package com.github.chen0040.si.utils;


import java.io.Serializable;
import java.util.List;


/**
 * Created by xschen on 12/6/2017.
 */
public interface PathCostFunction extends Serializable {
   double evaluate(List<Integer> path);
   double heuristicCost(int state1, int state2);
}
