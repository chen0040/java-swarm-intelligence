package com.github.chen0040.si.bees;


import com.github.chen0040.si.utils.CostFunction;
import com.github.chen0040.si.utils.CostFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.testng.Assert.*;


/**
 * Created by xschen on 10/6/2017.
 */
public class BeeSwarmUnitTest {

   private static final Logger logger = LoggerFactory.getLogger(BeeSwarmUnitTest.class);

   @Test
   public void test_Rosenbrock() {
      CostFunctions.Rosenbrock Rosenbrock = new CostFunctions.Rosenbrock();
      BeeMediator mediator = new BeeMediator();
      mediator.setUpperBounds(Rosenbrock.upperBounds());
      mediator.setLowerBounds(Rosenbrock.lowerBounds());
      mediator.setDimension(Rosenbrock.dimension());
      mediator.setCostFunction(Rosenbrock);

      BeeSwarm swarm = new BeeSwarm();
      swarm.setMediator(mediator);
      swarm.setMaxIterations(50);

      Bee bestSolution = swarm.solve();
      logger.info("best solution: {} cost: {}", bestSolution, bestSolution.getCost());

      List<Double> trend = swarm.getCostTrend();
      logger.info("trend: {}", trend);

      assertThat(bestSolution.getCost()).isCloseTo(0, within(0.001));

   }

   @Test
   public void test_Rastrigin() {
      CostFunctions.Rastrigin Rastrigin = new CostFunctions.Rastrigin();

      BeeMediator mediator = new BeeMediator();
      mediator.setUpperBounds(Rastrigin.upperBounds());
      mediator.setLowerBounds(Rastrigin.lowerBounds());
      mediator.setDimension(Rastrigin.dimension());
      mediator.setCostFunction(Rastrigin);

      BeeSwarm swarm = new BeeSwarm();
      swarm.setMediator(mediator);
      swarm.setMaxIterations(50);

      Bee bestSolution = swarm.solve();
      logger.info("best solution: {} cost: {}", bestSolution, bestSolution.getCost());

      List<Double> trend = swarm.getCostTrend();
      logger.info("trend: {}", trend);

      assertThat(bestSolution.getCost()).isCloseTo(0.00, within(0.1));
   }

   @Test
   public void test_Griewangk() {
      CostFunctions.Griewangk Griewangk = new CostFunctions.Griewangk();

      BeeMediator mediator = new BeeMediator();
      mediator.setUpperBounds(Griewangk.upperBounds());
      mediator.setLowerBounds(Griewangk.lowerBounds());
      mediator.setDimension(Griewangk.dimension());
      mediator.setCostFunction(Griewangk);

      BeeSwarm swarm = new BeeSwarm();
      swarm.setMediator(mediator);
      swarm.setMaxIterations(100);

      Bee bestSolution = swarm.solve();
      logger.info("best solution: {} cost: {}", bestSolution, bestSolution.getCost());

      List<Double> trend = swarm.getCostTrend();
      logger.info("trend: {}", trend);

      assertThat(bestSolution.getCost()).isCloseTo(0.00, within(0.1));
   }


}
