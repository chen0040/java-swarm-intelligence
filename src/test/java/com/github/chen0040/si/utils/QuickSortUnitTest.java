package com.github.chen0040.si.utils;


import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;


/**
 * Created by xschen on 10/6/2017.
 */
public class QuickSortUnitTest {

   @Test
   public void test_quickSort(){
      RandomGenerator randomGenerator = new RandomGeneratorImpl();
      List<Integer> a = new ArrayList<>();
      for(int i=0; i < 100; ++i){
         a.add(i);
      }
      KnuthShuffle.shuffle(a, randomGenerator);
      QuickSort.sort(a);

      assertTrue(SortUtil.isSorted(a));
   }
}
