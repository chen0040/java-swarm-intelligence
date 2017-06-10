package com.github.chen0040.si.utils;


import java.util.Comparator;
import java.util.List;


/**
 * Created by xschen on 10/6/2017.
 */
public class QuickSort {
   public static <T> void sort(List<T> a, Comparator<T> comparator){
      sort(a, 0, a.size()-1, comparator);
   }

   public static <T extends Comparable<T>> void sort(List<T> a) {
      sort(a, Comparable::compareTo);
   }

   public static <T> void sort(List<T> a, int lo, int hi, Comparator<T> comparator) {

      if(lo >= hi) return;

      if(hi - lo < 7) {
         InsertionSort.sort(a, lo, hi, comparator);
      }

      int j = partition(a, lo, hi, comparator);
      sort(a, lo, j-1, comparator);
      sort(a, j+1, hi, comparator);
   }

   private static <T> int partition(List<T> a, int lo, int hi, Comparator<T> comparator) {
      T v = a.get(lo);
      int i = lo, j = hi+1;
      while(true) {
         while(SortUtil.less(a.get(++i), v, comparator)){
            if(i >= hi) break;
         }
         while(SortUtil.less(v, a.get(--j), comparator)) {
            if(j <= lo) break;
         }

         if(i >= j) break;
         SortUtil.exchange(a, i, j);
      }

      SortUtil.exchange(a, lo, j);
      return j;
   }
}
