package com.github.chen0040.si;


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
}
