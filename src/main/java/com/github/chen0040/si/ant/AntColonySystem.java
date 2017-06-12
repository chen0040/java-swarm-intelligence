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
      List<TupleTwo<Integer,Integer>> path = mGlobalBestAnt.path();
      int segment_count = path.size();
      for (int i = 0; i < segment_count; ++i)
      {
         TupleTwo<Integer,Integer> state_transition = path.get(i);
         int state1_id = state_transition._1();
         int state2_id = state_transition._2();
         double pheromone = mPheromones.get(state1_id, state2_id);
         double p_delta = getRewardPerStateTransition(mGlobalBestAnt);
         pheromone += m_alpha * p_delta;

         mPheromones.set(state1_id, state2_id, pheromone);
         if (mSymmetric)
         {
            mPheromones.set(state2_id, state1_id, pheromone);
         }
      }
   }

   @Override
   public void transitStates(Ant ant, int state_index)
   {
      int current_state_id = ant.currentState();
      List<Integer> candidate_states = getCandidateNextStates(ant, current_state_id);

      if (candidate_states.isEmpty()) return;

      int selected_state_id = -1;
      double[] acc_prob = new double[candidate_states.size()];
      double product_sum = 0;
      double max_product = Double.NEGATIVE_INFINITY;

      int state_id_with_max_prob = -1;

      for (int i = 0; i < candidate_states.size(); ++i)
      {
         int candidate_state_id = candidate_states.get(i);
         double pheromone = mPheromones.get(current_state_id, candidate_state_id);
         double heuristic_cost = heuristicCost(current_state_id, candidate_state_id);

         double product = Math.pow(pheromone, m_alpha) * Math.pow(heuristic_cost, m_beta);

         product_sum += product;
         acc_prob[i] = product_sum;

         if (product > max_product)
         {
            max_product = product;
            state_id_with_max_prob = candidate_state_id;
         }
      }

      double r = mediator.nextDouble();
      if (r <= m_Q)
      {
         selected_state_id = state_id_with_max_prob;
      }
      else
      {
         r = mediator.nextDouble();
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

   protected void localPheromoneUpdate(int state1_id, int state2_id)
   {
      double pheromone = mPheromones.get(state1_id, state2_id);

      pheromone = (1 - m_rho) * pheromone + m_rho * mTau0;
      if (pheromone <= mTau0)
      {
         pheromone = mTau0;
      }

      mPheromones.set(state1_id, state2_id, pheromone);
      if (mSymmetric)
      {
         mPheromones.set(state2_id, state1_id, pheromone);
      }
   }
}
