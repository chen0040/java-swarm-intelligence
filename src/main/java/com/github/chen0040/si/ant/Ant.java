package com.github.chen0040.si.ant;


import com.github.chen0040.data.utils.TupleTwo;
import com.github.chen0040.si.utils.PathMediator;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xschen on 12/6/2017.
 * Ant class
 */
@Getter
@Setter
public class Ant {
   protected final List<Integer> visitedStates = new ArrayList<>();
   private boolean costValid = false;
   private double cost;

   public int currentState()
   {
      if(visitedStates.isEmpty()) {
         return -1;
      } else {
         return visitedStates.get(visitedStates.size()-1);
      }
   }

   public List<TupleTwo<Integer, Integer>> path()
   {
      if(visitedStates.isEmpty()) return new ArrayList<>();

      List<TupleTwo<Integer, Integer>> path = new ArrayList<>();
      for(int i = 0; i < this.visitedStates.size()-1; ++i)
      {
         int state1_id = this.visitedStates.get(i);
         int state2_id = this.visitedStates.get(i + 1);
         path.add(new TupleTwo<>(state1_id, state2_id));
      }

      return path;
   }

   public Ant()
   {
   }

   public void visit(int state)
   {
      visitedStates.add(state);
      costValid = false;
   }

   public void reset()
   {
      visitedStates.clear();
      costValid = false;
   }

   public int pathLength()
   {
      return visitedStates.size();
   }

   public boolean hasVisited(int state_id)
   {
      return visitedStates.contains(state_id);
   }

   public void copy(Ant rhs)
   {
      visitedStates.clear();
      visitedStates.addAll(rhs.visitedStates);
      costValid = rhs.costValid;
      cost = rhs.cost;
   }

   public boolean isBetterThan(Ant that) {
      if(!that.costValid) {
         return true;
      }
      return Double.compare(this.cost, that.cost) < 0;
   }


   public void evaluate(PathMediator mediator) {
      cost = mediator.evaluate(visitedStates);
      costValid = true;
   }
}
