package com.github.chen0040.si.utils;


import java.util.Comparator;
import java.util.List;


/**
 * Created by xschen on 10/6/2017.
 */
public class SortUtil {
   public static <T> void exchange(List<T> a, int i, int j){
      T temp = a.get(i);
      a.set(i, a.get(j));
      a.set(j, temp);
   }

   public static <T> boolean less(T a1, T a2, Comparator<T> comparator){
      return comparator.compare(a1,a2) < 0;
   }


   public static boolean isSorted(List<Integer> a) {
      for(int i=1; i < a.size(); ++i) {
         if(less(a.get(i), a.get(i-1), Integer::compare)){
            return false;
         }
      }
      return true;
   }
}
