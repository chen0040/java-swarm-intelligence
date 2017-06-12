package com.github.chen0040.si.ant;


import com.github.chen0040.data.utils.TupleTwo;
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
public class AntSystem {
   protected final List<Ant> mAnts = new ArrayList<>();
   protected Ant mGlobalBestAnt;

   protected double m_alpha=1;
   protected double m_beta=2;
   protected double m_Q=0.9;
   protected double m_rho=0.1;
   protected boolean mSymmetric = true;
   protected final SparseMatrix mPheromones = new SparseMatrix();
   protected int mStateCount;
   protected int populationSize;

   protected double mTau0;

   protected PathMediator mediator = new PathMediator();

   private double tolerance = -1; //0.000001;
   private int maxIterations = 100;

   protected double GetRewardPerStateTransition(Ant ant)
   {
      return 1.0 / ant.getCost();
   }

   public Ant GenerateAnt()
   {
      return new Ant();
   }

   public void Initialize()
   {
      mGlobalBestAnt = GenerateAnt();

      for (int i = 0; i < populationSize; ++i)
      {
         mAnts.add(GenerateAnt());
      }
      mTau0 = 1.0 / mStateCount;

      for (int i = 0; i < mStateCount; ++i)
      {
         for (int j = 0; j < mStateCount; ++j)
         {
            mPheromones.set(i, j, mTau0);
         }
      }
   }

   public void UpdateAntCost()
   {
      for (int i = 0; i < populationSize; ++i)
      {
         mAnts.get(i).evaluate(mediator);
      }
   }

   public Ant solve()
   {
      Initialize();
      int iteration = 0;
      double cost_reduction = tolerance;
      double global_best_solution_cost = Double.MAX_VALUE;
      double prev_global_best_solution_cost;
      while ((tolerance < 0 || cost_reduction >= tolerance) && iteration < maxIterations)
      {
         prev_global_best_solution_cost = global_best_solution_cost;
         Iterate();
         global_best_solution_cost = mGlobalBestAnt.getCost();
         cost_reduction = prev_global_best_solution_cost - global_best_solution_cost;
         iteration++;
      }

      return mGlobalBestAnt;
   }

   public void UpdateGlobalBestAnt()
   {
      Ant best_particle = null;
      for (int i = 0; i < populationSize; ++i)
      {
         if (mAnts.get(i).isBetterThan(mGlobalBestAnt))
         {
            best_particle = mAnts.get(i);
         }
      }
      if (best_particle != null)
      {
         mGlobalBestAnt.Copy(best_particle);
      }
   }

   public void Iterate()
   {
      AntTraverse();
      UpdateAntCost();
      EvaporatePheromone();
      UpdateGlobalBestAnt();
      DepositPheromone();
   }

   public void AntTraverse()
   {
      int ant_count = populationSize;

      for (int i = 0; i < ant_count; ++i)
      {
         mAnts.get(i).Reset();
      }

      for (int state_index = 0; state_index < mStateCount; ++state_index)
      {
         for (int i = 0; i < ant_count; ++i)
         {
            Ant ant = mAnts.get(i);
            TransiteState(ant, state_index);
         }
      }
   }

   public void DepositPheromone()
   {
      for (int ant_index = 0; ant_index < populationSize; ++ant_index)
      {
         Ant ant = mAnts.get(ant_index);

         List<TupleTwo<Integer, Integer>> path = ant.FindTrasitionPath();
         int segment_count = path.size();
         for (int i = 0; i < segment_count; ++i)
         {
            TupleTwo<Integer, Integer> state_transition = path.get(i);
            int state1_id = state_transition._1();
            int state2_id = state_transition._2();
            double pheromone = mPheromones.get(state1_id, state2_id);
            double p_delta = GetRewardPerStateTransition(ant);
            pheromone += m_alpha * p_delta;

            mPheromones.set(state1_id, state2_id, pheromone);
            if (mSymmetric)
            {
               mPheromones.set(state2_id, state1_id, pheromone);
            }
         }
      }
   }

   public void EvaporatePheromone()
   {
      double pheromone = 0;
      for (int i = 0; i < mStateCount; ++i)
      {
         for (int j = 0; j < mStateCount; ++j)
         {
            pheromone = mPheromones.get(i, j);
            pheromone = (1 - m_alpha) * pheromone;
            if (pheromone < mTau0)
            {
               pheromone = mTau0;
            }
            mPheromones.set(i, j, pheromone);
         }
      }
   }

   public List<Integer> getCandidateNextStates(Ant ant, int state_id)
   {
      List<Integer> set = mediator.getCandidateNextStates(ant.mData, state_id);
      if(set.isEmpty()) {
         List<Integer> candidate_states = new ArrayList<>();
         for (int i = 0; i < mStateCount; ++i) {
            if (!ant.HasTraversedState(state_id)) {
               candidate_states.add(i);
            }
         }
         return candidate_states;
      } else {
         return set;
      }
   }

   public double heuristicCost(int state1_id, int state2_id)
   {
      return mediator.heuristicCost(state1_id, state2_id);
   }

   public void TransiteState(Ant ant, int state_index)
   {
      int current_state_id = ant.getCurrentState();
      List<Integer> candidate_states = getCandidateNextStates(ant, current_state_id);

      if (candidate_states.isEmpty()) return;

      int selected_state_id = -1;
      double[] acc_prob = new double[candidate_states.size()];
      double product_sum = 0;

      for (int i = 0; i < candidate_states.size(); ++i)
      {
         int candidate_state_id = candidate_states.get(i);
         double pheromone = mPheromones.get(current_state_id, candidate_state_id);
         double heuristic_cost = heuristicCost(current_state_id, candidate_state_id);

         double product = Math.pow(pheromone, m_alpha) * Math.pow(heuristic_cost, m_beta);

         product_sum += product;
         acc_prob[i] = product_sum;
      }

      double r = mediator.nextDouble();
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
         ant.Add(selected_state_id);
      }
   }
}
