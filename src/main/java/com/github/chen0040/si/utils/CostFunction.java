package com.github.chen0040.si.utils;


import java.util.List;


/**
 * Created by xschen on 10/6/2017.
 */
public interface CostFunction {
   double evaluate(List<Double> solution, List<Double> lowerBounds, List<Double> upperBounds);
}
