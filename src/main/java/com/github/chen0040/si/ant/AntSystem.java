package com.github.chen0040.si.ant;


import com.github.chen0040.data.utils.CollectionUtils;
import com.github.chen0040.data.utils.TupleTwo;
import com.github.chen0040.si.utils.KnuthShuffle;
import com.github.chen0040.si.utils.PathCostFunction;
import com.github.chen0040.si.utils.PathMediator;
import com.github.chen0040.si.utils.SparseMatrix;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xschen on 12/6/2017.
 */
@Getter
@Setter
public class AntSystem extends PathMediator {
   protected final List<Ant> ants = new ArrayList<>();
   protected Ant globalBestAnt;

   protected double alpha =1;
   protected double beta =2;
   protected double Q =0.9;
   protected double rho =0.1;
   protected boolean symmetric = true;
   protected final SparseMatrix pheromones = new SparseMatrix();
   protected int stateCount;
   protected int populationSize = 100;

   protected double tau0;


   private double tolerance = -1; //0.000001;
   private int maxIterations = 100;

   protected int iteration = 0;

   private int localSearchIntensity = 50;

   private final List<Double> costTrend = new ArrayList<>();

   private boolean localSearchEnabled =  false;

   protected double getRewardPerStateTransition(Ant ant)
   {
      return this.getReward(ant.getPath(), ant.getCost());
   }

   public Ant generateAnt()
   {
      return new Ant();
   }

   public void initialize()
   {
      costTrend.clear();
      globalBestAnt = generateAnt();

      for (int i = 0; i < populationSize; ++i)
      {
         ants.add(generateAnt());
      }
      tau0 = 1.0 / stateCount;

      for (int i = 0; i < stateCount; ++i)
      {
         for (int j = 0; j < stateCount; ++j)
         {
            pheromones.set(i, j, tau0);
         }
      }
   }

   public void updateAntCost()
   {
      for (int i = 0; i < populationSize; ++i)
      {
         ants.get(i).evaluate(this);
      }
   }

   public Ant solve()
   {
      initialize();
      this.iteration = 0;
      double cost_reduction = tolerance;
      double global_best_solution_cost = Double.MAX_VALUE;
      double prev_global_best_solution_cost;
      while ((tolerance < 0 || cost_reduction >= tolerance) && iteration < maxIterations)
      {
         prev_global_best_solution_cost = global_best_solution_cost;
         iterate();
         global_best_solution_cost = globalBestAnt.getCost();
         cost_reduction = prev_global_best_solution_cost - global_best_solution_cost;
         iteration++;
      }

      return globalBestAnt;
   }

   public void updateGlobalBestAnt()
   {
      Ant best_particle = null;
      for (int i = 0; i < populationSize; ++i)
      {
         if (ants.get(i).isBetterThan(globalBestAnt))
         {
            best_particle = ants.get(i);
         }
      }
      if (best_particle != null)
      {
         globalBestAnt.copy(best_particle);
      }
   }

   public void iterate()
   {
      antTraverse();
      updateAntCost();
      antExploit();
      evaporatePheromone();
      updateGlobalBestAnt();
      depositPheromone();
      costTrend.add(globalBestAnt.getCost());
   }

   // local search
   public void antExploit(){
      if(!localSearchEnabled) return;
      for(int i=0; i < populationSize; ++i){
         List<Integer> path = ants.get(i).getPath();
         double pathCost0 = ants.get(i).getCost();
         double pathCost = pathCost0;
         if(path.size() < 3) continue;
         for(int j=0; j < path.size(); ++j){
            int K = -1;
            double maxGain = 0;
            for(int k = j+1; k < path.size(); ++k){
               double gain = this.getCostFunction().gain4Exchange(path, pathCost, j, k);
               if(gain > maxGain) {
                  maxGain = gain;
                  K = k;
               }
            }

            if(K != -1) {
               CollectionUtils.exchange(path, j, K);
            }
         }

         if(pathCost < pathCost0) {
            ants.get(i).update(path, pathCost);
         }
      }


   }

   public void antTraverse()
   {
      int ant_count = populationSize;

      List<Integer> positions = new ArrayList<>();

      for(int i=0; i < stateCount; ++i) {
         positions.add(i);
      }
      KnuthShuffle.shuffle(positions, this.getRandomGenerator());

      for (int i = 0; i < ant_count; ++i)
      {
         ants.get(i).reset();
         ants.get(i).visit(positions.get(i % stateCount));
      }



      for (int state_index = 1; state_index < stateCount; ++state_index)
      {
         for (int i = 0; i < ant_count; ++i)
         {
            Ant ant = ants.get(i);
            transitStates(ant);
         }
      }
   }

   public void depositPheromone()
   {
      for (int ant_index = 0; ant_index < populationSize; ++ant_index)
      {
         Ant ant = ants.get(ant_index);

         List<TupleTwo<Integer, Integer>> path = ant.path();
         int segment_count = path.size();
         for (int i = 0; i < segment_count; ++i)
         {
            TupleTwo<Integer, Integer> state_transition = path.get(i);
            int state1_id = state_transition._1();
            int state2_id = state_transition._2();
            double pheromone = pheromones.get(state1_id, state2_id);
            double p_delta = getRewardPerStateTransition(ant);
            pheromone += alpha * p_delta;

            pheromones.set(state1_id, state2_id, pheromone);
            if (symmetric)
            {
               pheromones.set(state2_id, state1_id, pheromone);
            }
         }
      }
   }

   public void evaporatePheromone()
   {
      double pheromone = 0;
      for (int i = 0; i < stateCount; ++i)
      {
         for (int j = 0; j < stateCount; ++j)
         {
            pheromone = pheromones.get(i, j);
            pheromone = (1 - alpha) * pheromone;
            if (pheromone < tau0)
            {
               pheromone = tau0;
            }
            pheromones.set(i, j, pheromone);
         }
      }
   }

   public List<Integer> getCandidateNextStates(Ant ant)
   {
      List<Integer> set = this.getCandidateNextStates(ant.path);
      if(set.isEmpty()) {
         List<Integer> candidate_states = new ArrayList<>();
         for (int i = 0; i < stateCount; ++i) {
            if (!ant.hasVisited(i)) {
               candidate_states.add(i);
            }
         }
         return candidate_states;
      } else {
         return set;
      }
   }

   public double heuristicValue(int state1_id, int state2_id)
   {
      return this.heuristicValue(state1_id, state2_id);
   }

   public void transitStates(Ant ant)
   {
      int current_state_id = ant.currentState();

      List<Integer> candidate_states = getCandidateNextStates(ant);

      if (candidate_states.isEmpty()) return;

      int selected_state_id = -1;
      double[] acc_prob = new double[candidate_states.size()];
      double product_sum = 0;

      for (int i = 0; i < candidate_states.size(); ++i)
      {
         int candidate_state_id = candidate_states.get(i);
         double pheromone = pheromones.get(current_state_id, candidate_state_id);
         double heuristic_value = heuristicValue(current_state_id, candidate_state_id);

         double product = Math.pow(pheromone, alpha) * Math.pow(heuristic_value, beta);

         product_sum += product;
         acc_prob[i] = product_sum;
      }

      double r = this.nextDouble();
      for (int i = 0; i < candidate_states.size(); ++i)
      {
         acc_prob[i] /= product_sum;
         if (r <= acc_prob[i])
         {
            selected_state_id = candidate_states.get(i);
            break;
         }
      }

      if (selected_state_id != -1)
      {
         ant.visit(selected_state_id);
      }
   }


   public void setProblemSize(int size) {
      stateCount = size;
   }

}
