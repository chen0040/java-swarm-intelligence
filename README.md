# java-swarm-intelligence

Optimization framework based on swarm intelligence

[![Build Status](https://travis-ci.org/chen0040/java-swarm-intelligence.svg?branch=master)](https://travis-ci.org/chen0040/java-swarm-intelligence) [![Coverage Status](https://coveralls.io/repos/github/chen0040/java-swarm-intelligence/badge.svg?branch=master)](https://coveralls.io/github/chen0040/java-swarm-intelligence?branch=master) [![Documentation Status](https://readthedocs.org/projects/java-swarm-intelligence/badge/?version=latest)](http://java-swarm-intelligence.readthedocs.io/en/latest/?badge=latest)


# Features

* Bees algorithm (Continuous Optimization)
* Ant Colony Optimization (Combinatorial Optimization)
* Particle Swarm Optimization (Continuous Optimization)

# Install

Add the following dependency to your POM file:

```xml
<dependency>
  <groupId>com.github.chen0040</groupId>
  <artifactId>java-swarm-intelligence</artifactId>
  <version>1.0.1</version>
</dependency>
```

# Usage

### Bees Swarm 

The sample code below shows how to use the bees algorithm to solve the Rosenbrock minimization problem:

```java
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
BeeMediator mediator = new BeeMediator();
mediator.setUpperBounds(Arrays.asList(5.0, 5.0));
mediator.setLowerBounds(Arrays.asList(-5.0, -5.0));
mediator.setDimension(2);
mediator.setCostFunction(Rosenbrock);

BeeSwarm swarm = new BeeSwarm();
swarm.setMediator(mediator);
swarm.setMaxIterations(50);

Bee bestSolution = swarm.solve();
logger.info("best solution: {} cost: {}", bestSolution, bestSolution.getCost());

List<Double> trend = swarm.getCostTrend();
logger.info("trend: {}", trend);
```




