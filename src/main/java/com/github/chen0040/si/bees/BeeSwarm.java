package com.github.chen0040.si.bees;


import com.github.chen0040.si.utils.QuickSort;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
public class BeeSwarm implements Serializable {
   private static final long serialVersionUID = -771177914366258940L;

   protected int scoutBeeCount = 60; //number of scout bees (e.g. 40-1000)

   protected int bestPatchCount = 20; //number of best selected patches (e.g. 10-50)
   protected int elitePatchCount = 10; //number of elite selected patches (e.g. 10-50)
   protected int beeCountOnBestPatches = 15; //number of recruited bees around best selected patches (e.g. 10-50)
   protected int beeCountOnElitePatches = 30; //number of recruited bees around elite selected patches (e.g. 10-50)
   protected final List<Bee> patches = new ArrayList<>();
   private Bee globalBestSolution = null;

   private double tolerance = 0.000001;
   private int maxIterations = 100000;

   private BeeMediator mediator = new BeeMediator();

   public void initialize()
   {
     patches.clear();

     for(int i=0; i < scoutBeeCount; ++i){
         Bee bee = new Bee();
         bee.initialize(mediator);
         bee.randomSearch(mediator);
         patches.add(new Bee());
     }

     QuickSort.sort(patches, Comparator.comparingDouble(Bee::getCost));
     globalBestSolution = patches.get(0).makeCopy();
   }


   public Bee solve()
   {
     initialize();
     int iteration = 0;
     double cost_reduction = tolerance;
     double global_best_solution_cost = globalBestSolution.getCost();
     double prev_global_best_solution_cost = global_best_solution_cost;
     while (cost_reduction >= tolerance && iteration < maxIterations)
     {
         prev_global_best_solution_cost = global_best_solution_cost;
         iterate();
         global_best_solution_cost = globalBestSolution.getCost();
         cost_reduction = prev_global_best_solution_cost - global_best_solution_cost;
         iteration++;
     }

     return globalBestSolution;
   }

   public void iterate()
   {
     //scout at elite selected patches
     for (int j = 0; j < elitePatchCount; ++j) //number of elite selected patches
     {
         //the following for loop is equivalent to a local search around each solution in the elite solutions
         for (int i = 0; i < beeCountOnElitePatches; ++i)
         {
             Bee bee = patches.get(j).dance(mediator);
             if (bee.isBetterThan(patches.get(j)))
             {
                 patches.set(j, bee.makeCopy());
             }
         }
     }

     //scout at best selected patches next best to the elite (therefore patch count=(_m - _e))
     for (int j = elitePatchCount; j < bestPatchCount; ++j)
     {
         //the following for loop is equivalent to a local search around each solution next best to the elite solutions
         for (int i = 0; i < beeCountOnBestPatches; ++i)
         {
               Bee bee = patches.get(j).dance(mediator);
             if (bee.isBetterThan(patches.get(j)))
             {
                 patches.set(j, bee);
             }
         }
     }

     //assign remaining bees to search randomly
     for (int j = bestPatchCount; j < scoutBeeCount; ++j)
     {
         globalBestSolution.randomSearch(mediator);
         patches.set(j, globalBestSolution.makeCopy());
     }

     QuickSort.sort(patches, Comparator.comparingDouble(Bee::getCost));
     globalBestSolution = patches.get(0).makeCopy();
   }
}
