package com.github.chen0040.si;


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
public class BeeMediator implements Serializable {
   private static final long serialVersionUID = -3133404010260276170L;
   protected List<Double> lowerBounds = new ArrayList<>();
   protected List<Double> upperBounds = new ArrayList<>();

   protected Serializable constraints = null;

   private CostFunction costFunction = null;

   private RandomGenerator generator = new RandomGeneratorImpl();


   public double evaluate(List<Double> solution, List<Double> lowerBounds, List<Double> upperBounds) {
      return costFunction.evaluate(solution, lowerBounds, upperBounds);
   }


   public double randomWithinBounds(int index) {
      double lowerBound = lowerBounds.get(index);
      double upperBound = upperBounds.get(index);
      return lowerBound + generator.nextDouble() * (upperBound - lowerBound);
   }


   public double mutateWithinBounds(int index, Double original) {
      return 0;
   }
}
