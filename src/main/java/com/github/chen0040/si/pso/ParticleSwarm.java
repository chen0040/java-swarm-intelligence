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
public class ParticleSwarm extends Mediator {
   protected final List<Particle> particles = new ArrayList<>();
   protected final List<Particle> localBestParticles = new ArrayList<>();
   protected Particle globalBestSolution = null;

   protected double C1 = 1;
   protected double C2 = 2;

   private int populationSize = 1000;

   private double tolerance = -1; //0.000001;
   private int maxIterations = 100;

   private List<Double> costTrend = new ArrayList<>();


   protected Particle create(){
      Particle p = new Particle();
      p.initialize(this);
      return p;
   }

   public void Initialize()
   {
      particles.clear();
      localBestParticles.clear();
      costTrend.clear();

      globalBestSolution = create();
      globalBestSolution.evaluate(this);
      for (int i = 0; i < populationSize; ++i)
      {
         Particle p = create();
         particles.add(p);
         localBestParticles.add(p.makeCopy());
      }

      updateParticleCosts();
      updateLocalBestParticles();
      updateGlobalBestParticle();
   }

   public void updateParticleCosts()
   {
      for (int i = 0; i < particles.size(); ++i)
      {
         particles.get(i).evaluate(this);
      }
   }

   public void updateLocalBestParticles()
   {
      for(int i = 0; i < particles.size(); ++i)
      {
         if (particles.get(i).isBetterThan(localBestParticles.get(i)))
         {
            localBestParticles.get(i).copy(particles.get(i));
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
         global_best_solution_cost = globalBestSolution.getCost();
         cost_reduction = prev_global_best_soution_cost - global_best_solution_cost;
         iteration++;
      }

      return globalBestSolution;
   }



   public void updateParticleVelocity()
   {
      int dimension = this.getDimension();
      for (int i = 0; i < particles.size(); ++i)
      {
         for (int j = 0; j < dimension; ++j)
         {
            double oldV = particles.get(i).getVelocity(j);
            double Xj = particles.get(i).getPosition(j);
            double X_lbest = localBestParticles.get(i).getPosition(j);
            double X_gbest = globalBestSolution.getPosition(j);

            double r1 = this.nextDouble();
            double r2 = this.nextDouble();
            double r3 = this.nextDouble();

            double w = 0.5 + r3 / 2;
            double newV = w * oldV + C1 * r1 * (X_lbest - Xj) + C2 * r2 * (X_gbest - Xj);

            particles.get(i).setVelocity(j, newV);
         }
      }
   }

   public void updateParticlePosition()
   {
      int dimension = this.getDimension();
      for (int i = 0; i < particles.size(); ++i)
      {
         for (int j = 0; j < dimension; ++j)
         {
            double Vj = particles.get(i).getVelocity(j);
            double Xj = particles.get(i).getPosition(j);

            particles.get(i).setPosition(j, Xj + Vj);
         }
      }
   }

   public void updateGlobalBestParticle()
   {
      Particle best_particle = null;
      for (int i = 0; i < particles.size(); ++i)
      {
         if (particles.get(i).isBetterThan(globalBestSolution))
         {
            best_particle = particles.get(i);
         }
      }
      if (best_particle != null)
      {
         globalBestSolution.copy(best_particle);
      }
   }

   public void iterate()
   {
      updateParticleVelocity();
      updateParticlePosition();
      updateParticleCosts();
      updateLocalBestParticles();
      updateGlobalBestParticle();
      costTrend.add(globalBestSolution.getCost());
   }
}
