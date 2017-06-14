package com.github.chen0040.si.utils;


import com.github.chen0040.data.utils.CollectionUtils;

import java.io.Serializable;
import java.util.List;


/**
 * Created by xschen on 12/6/2017.
 */
public interface PathCostFunction extends Serializable {
   double evaluate(List<Integer> path);
   double stateTransitionWeight(int state1, int state2);

   default double getReward(List<Integer> path, double cost) {
      return 1.0 / cost;
   }

   default double gain4Exchange(List<Integer> path, double pathCost, int i, int j) {
      List<Integer> mutated = CollectionUtils.clone(path, x -> x);

      CollectionUtils.exchange(mutated, i, j);
      return pathCost - evaluate(mutated);
   }
}
