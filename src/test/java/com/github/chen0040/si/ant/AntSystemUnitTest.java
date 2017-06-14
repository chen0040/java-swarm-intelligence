package com.github.chen0040.si.ant;


import com.github.chen0040.benchmarks.Tsp;
import com.github.chen0040.benchmarks.TspBenchmark;
import com.github.chen0040.si.utils.PathCostFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;


/**
 * Created by xschen on 14/6/2017.
 */
public class AntSystemUnitTest {

   private static final Logger logger = LoggerFactory.getLogger(AntSystemUnitTest.class);

   @Test
   public void test_tsp(){

      TspBenchmark benchmark = Tsp.get(Tsp.Instance.bayg29);

      PathCostFunction costFunction = new PathCostFunction() {
         @Override public double evaluate(List<Integer> path) {
            double cost = 0;
            for(int i=0; i < path.size(); ++i) {
               int j = (i+1) % path.size();
               double distance = benchmark.distance(path.get(i), path.get(j));
               cost += distance;
            }
            return cost;
         }

         // heuristic weight for transition from state1 to state2 during path construction
         // the higher the weight the more favorable to transit from state1 to state2
         @Override public double stateTransitionWeight(int state1, int state2) {
            return 1 / (1 + benchmark.distance(state1, state2));
         }
      };



      AntSystem antSystem = new AntSystem();
      antSystem.setProblemSize(benchmark.size());
      antSystem.setCostFunction(costFunction);

      antSystem.setMaxIterations(100);

      Ant bestAnt = antSystem.solve();

      System.out.println("minimal total distance found: " + bestAnt.getCost());
      System.out.println("best known cost: " + costFunction.evaluate(benchmark.optTour()));

      System.out.println("best path found: ");
      for(int i=0; i < bestAnt.getPath().size(); ++i) {
         int j = (i + 1) % bestAnt.getPath().size();
         System.out.println(bestAnt.getPath().get(i) + " => " + bestAnt.getPath().get(j));
      }

      logger.info("cost trends: {}", antSystem.getCostTrend());


   }

}
