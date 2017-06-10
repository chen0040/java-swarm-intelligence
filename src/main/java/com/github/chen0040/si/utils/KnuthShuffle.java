package com.github.chen0040.si.utils;


import java.util.List;


/**
 * Created by xschen on 10/6/2017.
 */
public class KnuthShuffle {
   public static <T> void shuffle(List<T> a, RandomGenerator randomGenerator) {
      if(a.size() <= 1) return;
      for(int i=1; i < a.size(); ++i) {
         int j = randomGenerator.nextInt(i+1);
         SortUtil.exchange(a, j, i);
      }
   }
}
