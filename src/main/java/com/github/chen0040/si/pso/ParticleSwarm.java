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
public class ParticleSwarm {
   protected final List<Particle> mParticles = new ArrayList<>();
   protected final List<Particle> mLocalBestParticles = new ArrayList<>();
   protected Particle mGlobalBestSolution = null;

   protected double mC1 = 1;
   protected double mC2 = 2;

   private int populationSize = 1000;

   private double tolerance = -1; //0.000001;
   private int maxIterations = 100;


   private Mediator mediator = new Mediator();

   protected Particle create(){
      Particle p = new Particle();
      p.initialize(mediator);
      return p;
   }

   public void Initialize()
   {
      mGlobalBestSolution = create();
      mGlobalBestSolution.evaluate(mediator);
      for (int i = 0; i < populationSize; ++i)
      {
         Particle p = create();
         mParticles.add(p);
         mLocalBestParticles.add(p.makeCopy());
      }

      updateParticleCosts();
      updateLocalBestParticles();
      updateGlobalBestParticle();
   }

   public void updateParticleCosts()
   {
      for (int i = 0; i < mParticles.size(); ++i)
      {
         mParticles.get(i).evaluate(mediator);
      }
   }

   public void updateLocalBestParticles()
   {
      for(int i=0; i < mParticles.size(); ++i)
      {
         if (mParticles.get(i).isBetterThan(mLocalBestParticles.get(i)))
         {
            mLocalBestParticles.get(i).copy(mParticles.get(i));
         }
      }
   }


   public Particle solve()
   {
      Initialize();
      int iteration = 0;
      double cost_reduction = tolerance;
      double global_best_solution_cost = Double.MAX_VALUE;
      double prev_global_best_soution_cost = global_best_solution_cost;
      while ((tolerance < 0 || cost_reduction >= tolerance) && iteration < maxIterations)
      {
         prev_global_best_soution_cost = global_best_solution_cost;
         iterate();
         global_best_solution_cost = mGlobalBestSolution.getCost();
         cost_reduction = prev_global_best_soution_cost - global_best_solution_cost;
         iteration++;
      }

      return mGlobalBestSolution;
   }



   public void updateParticleVelocity()
   {
      int dimension = mediator.getDimension();
      for (int i = 0; i < mParticles.size(); ++i)
      {
         for (int j = 0; j < dimension; ++j)
         {
            double oldV = mParticles.get(i).getVelocity(j);
            double Xj = mParticles.get(i).getPosition(j);
            double X_lbest = mLocalBestParticles.get(i).getPosition(j);
            double X_gbest = mGlobalBestSolution.getPosition(j);

            double r1 = mediator.nextDouble();
            double r2 = mediator.nextDouble();
            double r3 = mediator.nextDouble();

            double w = 0.5 + r3 / 2;
            double newV = w * oldV + mC1 * r1 * (X_lbest - Xj) + mC2 * r2 * (X_gbest - Xj);

            mParticles.get(i).setVelocity(j, newV);
         }
      }
   }

   public void updateParticlePosition()
   {
      int dimension = mediator.getDimension();
      for (int i = 0; i < mParticles.size(); ++i)
      {
         for (int j = 0; j < dimension; ++j)
         {
            double Vj = mParticles.get(i).getVelocity(j);
            double Xj = mParticles.get(i).getPosition(j);

            mParticles.get(i).setPosition(j, Xj + Vj);
         }
      }
   }

   public void updateGlobalBestParticle()
   {
      Particle best_particle = null;
      for (int i = 0; i < mParticles.size(); ++i)
      {
         if (mParticles.get(i).isBetterThan(mGlobalBestSolution))
         {
            best_particle = mParticles.get(i);
         }
      }
      if (best_particle != null)
      {
         mGlobalBestSolution.copy(best_particle);
      }
   }

   public void iterate()
   {
      updateParticleVelocity();
      updateParticlePosition();
      updateParticleCosts();
      updateLocalBestParticles();
      updateGlobalBestParticle();
   }
}
