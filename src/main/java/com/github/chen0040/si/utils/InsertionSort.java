package com.github.chen0040.si.utils;


import java.util.Comparator;
import java.util.List;


/**
 * Created by xschen on 10/6/2017.
 */
public class InsertionSort {
   public static <T> void sort(List<T> a, int lo, int hi, Comparator<T> comparator) {
      if(lo >= hi) return;
      for(int i=lo; i <= hi; ++i){
         for(int j=i; j > lo; --j) {
            if(SortUtil.less(a.get(j), a.get(j-1), comparator)){
               SortUtil.exchange(a, j, j-1);
            } else {
               break;
            }
         }
      }
   }
}
