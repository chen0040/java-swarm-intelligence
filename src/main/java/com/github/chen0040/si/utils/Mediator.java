package com.github.chen0040.si.utils;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by xschen on 10/6/2017.
 */
@Getter
@Setter
public class Mediator implements Serializable {
   private static final long serialVersionUID = -3133404010260276170L;
   protected List<Double> lowerBounds = new ArrayList<>();
   protected List<Double> upperBounds = new ArrayList<>();

   protected Serializable constraints = null;

   private CostFunction costFunction = null;

   private RandomGenerator randomGenerator = new RandomGeneratorImpl();

   private double mutateSd = 1.0;

   private int dimension = 2;

   public double evaluate(List<Double> solution) {
      return costFunction.evaluate(solution, lowerBounds, upperBounds);
   }


   public double randomWithinBounds(int index) {
      double lowerBound = lowerBounds.get(index);
      double upperBound = upperBounds.get(index);
      return lowerBound + randomGenerator.nextDouble() * (upperBound - lowerBound);
   }


   public double mutateWithinBounds(int index, Double original) {
      return Math.min(upperBounds.get(index), Math.max(lowerBounds.get(index), original + randomGenerator.normal() * mutateSd));
   }


   public double nextDouble() {
      return randomGenerator.nextDouble();
   }
}
