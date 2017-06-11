package com.github.chen0040.si.pso;

import com.github.chen0040.si.utils.Mediator;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xschen on 10/6/2017.
 */
@Getter
@Setter
public class Particle {
   protected final List<Double> position = new ArrayList<>();
   protected final List<Double> velocity = new ArrayList<>();

   private double cost;
   private boolean costValid;

   public Particle()
   {

   }
   
   public Particle makeCopy()
   {
      Particle clone = new Particle();
      clone.copy(this);
      return clone;
   }

   public double getPosition(int index) {
      return position.get(index);
   }

   public void setPosition(int index, double value) {
      if(position.get(index) != value) {
         position.set(index,value);
         costValid = false;
      }
   }

   public double getVelocity(int index) {
      return velocity.get(index);
   }

   public void setVelocity(int index, double value) {
      if(velocity.get(index) != value) {
         velocity.set(index, value);
         costValid = false;
      }
   }


   public void copy(Particle rhs)
   {
      position.clear();
      velocity.clear();
      position.addAll(rhs.getPosition());
      velocity.addAll(rhs.getVelocity());
      cost = rhs.cost;
      costValid = rhs.costValid;
   }

   public boolean isBetterThan(Particle that) {
      return Double.compare(this.cost, that.cost) < 0;
   }


   public void initialize(Mediator mediator) {
      int dimension = mediator.getDimension();
      velocity.clear();
      position.clear();

      for (int d = 0; d < dimension; ++d)
      {
         position.add(mediator.randomWithinBounds(d));
         velocity.add(0.0);
      }
   }


   public void evaluate(Mediator mediator) {
      cost = mediator.evaluate(position);
      costValid = true;
   }
}
