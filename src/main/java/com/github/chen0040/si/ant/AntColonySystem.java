package com.github.chen0040.si.ant;


import com.github.chen0040.data.utils.TupleTwo;

import java.util.List;


/**
 * Created by xschen on 12/6/2017.
 */
public class AntColonySystem extends AntSystem {
   @Override
   public void depositPheromone()
   {
      List<TupleTwo<Integer,Integer>> path = globalBestAnt.path();
      int segment_count = path.size();
      for (int i = 0; i < segment_count; ++i)
      {
         TupleTwo<Integer,Integer> state_transition = path.get(i);
         int state1_id = state_transition._1();
         int state2_id = state_transition._2();
         double pheromone = pheromones.get(state1_id, state2_id);
         double p_delta = getRewardPerStateTransition(globalBestAnt);
         pheromone += alpha * p_delta;

         pheromones.set(state1_id, state2_id, pheromone);
         if (symmetric)
         {
            pheromones.set(state2_id, state1_id, pheromone);
         }
      }
   }

   @Override
   public void transitStates(Ant ant)
   {
      int current_state_id = ant.currentState();
      List<Integer> candidate_states = getCandidateNextStates(ant);

      if (candidate_states.isEmpty()) return;

      int selected_state_id = -1;
      double[] acc_prob = new double[candidate_states.size()];
      double product_sum = 0;
      double max_product = Double.NEGATIVE_INFINITY;

      int state_id_with_max_prob = -1;

      for (int i = 0; i < candidate_states.size(); ++i)
      {
         int candidate_state_id = candidate_states.get(i);
         double pheromone = pheromones.get(current_state_id, candidate_state_id);
         double heuristic_cost = heuristicValue(current_state_id, candidate_state_id);

         double product = Math.pow(pheromone, alpha) * Math.pow(heuristic_cost, beta);

         product_sum += product;
         acc_prob[i] = product_sum;

         if (product > max_product)
         {
            max_product = product;
            state_id_with_max_prob = candidate_state_id;
         }
      }

      double r = this.nextDouble();
      if (r <= Q)
      {
         selected_state_id = state_id_with_max_prob;
      }
      else
      {
         r = this.nextDouble();
         for (int i = 0; i < candidate_states.size(); ++i)
         {
            acc_prob[i] /= product_sum;
            if (r <= acc_prob[i])
            {
               selected_state_id = candidate_states.get(i);
               break;
            }
         }
      }

      if (selected_state_id != -1)
      {
         ant.visit(selected_state_id);
         localPheromoneUpdate(current_state_id, selected_state_id);
      }
   }

   private void localPheromoneUpdate(int state1_id, int state2_id)
   {
      double pheromone = pheromones.get(state1_id, state2_id);

      pheromone = (1 - rho) * pheromone + rho * tau0;
      if (pheromone <= tau0)
      {
         pheromone = tau0;
      }

      pheromones.set(state1_id, state2_id, pheromone);
      if (symmetric)
      {
         pheromones.set(state2_id, state1_id, pheromone);
      }
   }
}
