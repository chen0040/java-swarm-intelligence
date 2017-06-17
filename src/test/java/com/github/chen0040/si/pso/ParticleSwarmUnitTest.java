package com.github.chen0040.si.pso;


import com.github.chen0040.si.pso.Particle;
import com.github.chen0040.si.pso.ParticleSwarm;
import com.github.chen0040.si.pso.ParticleSwarmUnitTest;
import com.github.chen0040.si.utils.CostFunction;
import com.github.chen0040.si.utils.CostFunctions;
import com.github.chen0040.si.utils.Mediator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.testng.Assert.*;


/**
 * Created by xschen on 11/6/2017.
 */
public class ParticleSwarmUnitTest {

   private static final Logger logger = LoggerFactory.getLogger(ParticleSwarmUnitTest.class);

   @Test
   public void test_Rosenbrock() {
      CostFunctions.Rosenbrock Rosenbrock = new CostFunctions.Rosenbrock();
      ParticleSwarm swarm = new ParticleSwarm();
      swarm.setUpperBounds(Rosenbrock.upperBounds());
      swarm.setLowerBounds(Rosenbrock.lowerBounds());
      swarm.setDimension(Rosenbrock.dimension());
      swarm.setCostFunction(Rosenbrock);
      swarm.setMaxIterations(50);

      Particle bestSolution = swarm.solve();
      logger.info("best solution: {} cost: {}", bestSolution, bestSolution.getCost());

      List<Double> trend = swarm.getCostTrend();
      logger.info("trend: {}", trend);

      assertThat(bestSolution.getCost()).isCloseTo(0, within(0.01));

   }

   @Test
   public void test_Rosenbrock2() {
      CostFunction Rosenbrock = new CostFunction() {
         public double calc(double x, double y)
         {
            double expr1 = (x*x - y);
            double expr2 = 1 - x;
            return 100 * expr1*expr1 + expr2*expr2;
         }
         @Override public double evaluate(List<Double> solution, List<Double> lowerBounds, List<Double> upperBounds) {
            return calc(solution.get(0), solution.get(1));
         }
      };

      ParticleSwarm swarm = new ParticleSwarm();
      swarm.setUpperBounds(Arrays.asList(5.0, 5.0));
      swarm.setLowerBounds(Arrays.asList(-5.0, -5.0));
      swarm.setDimension(2);
      swarm.setCostFunction(Rosenbrock);
      swarm.setMaxIterations(50);

      Particle bestSolution = swarm.solve();
      logger.info("best solution: {} cost: {}", bestSolution, bestSolution.getCost());

      List<Double> trend = swarm.getCostTrend();
      logger.info("trend: {}", trend);

      assertThat(bestSolution.getCost()).isCloseTo(0, within(0.01));

   }

   @Test
   public void test_Rastrigin() {
      CostFunctions.Rastrigin Rastrigin = new CostFunctions.Rastrigin();

      ParticleSwarm swarm = new ParticleSwarm();
      swarm.setUpperBounds(Rastrigin.upperBounds());
      swarm.setLowerBounds(Rastrigin.lowerBounds());
      swarm.setDimension(Rastrigin.dimension());
      swarm.setCostFunction(Rastrigin);
      swarm.setMaxIterations(50);

      Particle bestSolution = swarm.solve();
      logger.info("best solution: {} cost: {}", bestSolution, bestSolution.getCost());

      List<Double> trend = swarm.getCostTrend();
      logger.info("trend: {}", trend);

      assertThat(bestSolution.getCost()).isCloseTo(0.00, within(0.1));
   }

   @Test
   public void test_Griewangk() {
      CostFunctions.Griewangk Griewangk = new CostFunctions.Griewangk();

      ParticleSwarm swarm = new ParticleSwarm();
      swarm.setUpperBounds(Griewangk.upperBounds());
      swarm.setLowerBounds(Griewangk.lowerBounds());
      swarm.setDimension(Griewangk.dimension());
      swarm.setCostFunction(Griewangk);
      swarm.setMaxIterations(100);

      Particle bestSolution = swarm.solve();
      logger.info("best solution: {} cost: {}", bestSolution, bestSolution.getCost());

      List<Double> trend = swarm.getCostTrend();
      logger.info("trend: {}", trend);

      assertThat(bestSolution.getCost()).isCloseTo(0.00, within(0.1));
   }
}
