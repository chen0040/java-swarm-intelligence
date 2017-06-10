package com.github.chen0040.si.utils;


import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.Random;


/**
 * Created by xschen on 10/6/2017.
 */
public class RandomGeneratorImpl implements RandomGenerator {
   private static final long serialVersionUID = 8863580512109291828L;
   private Random random = new Random();


   @Override public double nextDouble() {
      return random.nextDouble();
   }

   @Override public double normal() {
      NormalDistribution normalDistribution = new NormalDistribution();
      return normalDistribution.sample();
   }


   @Override public int nextInt(int upper) {
      return random.nextInt(upper);
   }
}
