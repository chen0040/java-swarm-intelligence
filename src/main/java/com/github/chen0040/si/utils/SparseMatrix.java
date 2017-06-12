package com.github.chen0040.si.utils;


import java.util.HashMap;
import java.util.Map;


/**
 * Created by xschen on 12/6/2017.
 */
public class SparseMatrix {
   private Map<String, Double> values = new HashMap<>();

   private boolean isZero(double value) {
      return Math.abs(value) < 1e-15;
   }

   public void set(int row, int column, double value){
      String key = key(row, column);
      if(isZero(value)) {
         values.remove(key);
      } else {
         values.put(key, value);
      }
   }

   public double get(int row, int column) {
      return values.getOrDefault(key(row,column), 0.0);
   }

   private String key(int row, int column){
      return row + "-" + column;
   }

}
