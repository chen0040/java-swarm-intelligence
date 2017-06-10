package com.github.chen0040.si.utils;


import java.io.Serializable;


/**
 * Created by xschen on 10/6/2017.
 */
public interface RandomGenerator extends Serializable {
   double nextDouble();
   double normal();

   int nextInt(int upper);
}
