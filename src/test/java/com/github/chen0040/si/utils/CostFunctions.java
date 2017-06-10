package com.github.chen0040.si.utils;


import java.util.Arrays;
import java.util.List;


/**
 * Created by xschen on 10/6/2017.
 */
public class CostFunctions {
   public static class Rosenbrock implements CostFunction
   {
      public double calcFitness(double x, double y)
      {
         double expr1 = (x*x - y);
         double expr2 = 1 - x;
         return 100 * expr1*expr1 + expr2*expr2;
      }


      @Override public double evaluate(List<Double> solution, List<Double> lowerBounds, List<Double> upperBounds) {
         return calcFitness(solution.get(0), solution.get(1));
      }

      public List<Double> lowerBounds() {
         return Arrays.asList(-5.0, -5.0);
      }

      public List<Double> upperBounds() {
         return Arrays.asList(5.0, 5.0);
      }

      public int dimension(){
         return 2;
      }

   }
}
