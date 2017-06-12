package com.github.chen0040.si.ant;


import com.github.chen0040.data.utils.TupleTwo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xschen on 12/6/2017.
 * Ant class
 */
public class Ant {
   protected final List<Integer> mData = new ArrayList<>();
   private boolean costValid = false;
   private double cost;

   public int getCurrentState()
   {
      if(mData.isEmpty()) {
         return -1;
      } else {
         return mData.get(mData.size()-1);
      }
   }

   public List<TupleTwo<Integer, Integer>> FindTrasitionPath()
   {
      if(mData.isEmpty()) return new ArrayList<>();

      List<TupleTwo<Integer, Integer>> path = new ArrayList<>();
      for(int i=0; i < mData.size()-1; ++i)
      {
         int state1_id = mData.get(i);
         int state2_id = mData.get(i + 1);
         path.add(new TupleTwo<>(state1_id, state2_id));
      }

      return path;
   }

   public Ant()
   {
   }

   public void Add(int state)
   {
      mData.add(state);
      costValid = false;
   }

   public void Reset()
   {
      mData.clear();
      costValid = false;
   }

   public int pathLength()
   {
      return mData.size();
   }

   public boolean HasTraversedState(int state_id)
   {
      return mData.contains(state_id);
   }

   public int getState(int index) {
      return mData.get(index);
   }

   public void setState(int index, int value) {
      if(mData.get(index) != value) {
         mData.set(index, value);
         costValid = false;
      }
   }

   public Ant makeCopy()
   {
      Ant clone = new Ant();
      clone.Copy(this);
      return clone;
   }

   public void Copy(Ant rhs)
   {
      mData.clear();
      mData.addAll(rhs.mData);
      costValid = rhs.costValid;
      cost = rhs.cost;
   }
}
