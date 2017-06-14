package com.github.chen0040.si.ant;


import com.github.chen0040.data.utils.TupleTwo;
import com.github.chen0040.si.utils.PathMediator;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by xschen on 12/6/2017.
 * Ant class
 */
@Getter
@Setter
public class Ant {
   protected final List<Integer> path = new ArrayList<>();
   protected final Set<Integer> visited = new HashSet<>();
   private boolean costValid = false;
   private double cost;

   public int currentState()
   {
      if(path.isEmpty()) {
         return -1;
      } else {
         return path.get(path.size()-1);
      }
   }

   public List<TupleTwo<Integer, Integer>> path()
   {
      if(path.isEmpty()) return new ArrayList<>();

      List<TupleTwo<Integer, Integer>> path = new ArrayList<>();
      for(int i = 0; i < this.path.size()-1; ++i)
      {
         int state1_id = this.path.get(i);
         int state2_id = this.path.get(i + 1);
         path.add(new TupleTwo<>(state1_id, state2_id));
      }

      return path;
   }

   public Ant()
   {
   }

   public void visit(int state)
   {
      path.add(state);
      visited.add(state);
      costValid = false;
   }

   public void reset()
   {
      path.clear();
      visited.clear();
      costValid = false;
   }


   public boolean hasVisited(int state_id)
   {
      return visited.contains(state_id);
   }

   public void copy(Ant rhs)
   {
      path.clear();
      path.addAll(rhs.path);
      visited.clear();
      visited.addAll(rhs.visited);

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
      cost = mediator.evaluate(path);
      costValid = true;
   }


   public void update(List<Integer> path, double pathCost) {
      this.path.clear();
      this.path.addAll(path);
      this.visited.clear();
      this.visited.addAll(path);
      this.cost = pathCost;
      this.costValid = true;
   }
}
