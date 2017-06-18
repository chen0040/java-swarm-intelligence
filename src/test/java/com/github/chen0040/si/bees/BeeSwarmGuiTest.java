package com.github.chen0040.si.bees;


import com.github.chen0040.plt.CostTrend;
import com.github.chen0040.si.utils.CostFunctions;

import java.util.List;


/**
 * Created by xschen on 18/6/2017.
 */
public class BeeSwarmGuiTest {
   public static void main(String[] args) {
      CostFunctions.Griewangk Griewangk = new CostFunctions.Griewangk();

      BeeSwarm swarm = new BeeSwarm();
      swarm.setUpperBounds(Griewangk.upperBounds());
      swarm.setLowerBounds(Griewangk.lowerBounds());
      swarm.setDimension(Griewangk.dimension());
      swarm.setCostFunction(Griewangk);
      swarm.setMaxIterations(100);

      Bee bestSolution = swarm.solve();

      List<Double> trend = swarm.getCostTrend();
      CostTrend chart = new CostTrend(trend, "Cost vs Generation");
      chart.showIt(true);
   }
}
