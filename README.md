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
  <version>1.0.5</version>
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
BeeSwarm swarm = new BeeSwarm();
swarm.setUpperBounds(Arrays.asList(5.0, 5.0));
swarm.setLowerBounds(Arrays.asList(-5.0, -5.0));
swarm.setDimension(2);
swarm.setCostFunction(Rosenbrock);
swarm.setMaxIterations(50);

Bee bestSolution = swarm.solve();
logger.info("best solution: {} cost: {}", bestSolution, bestSolution.getCost());

List<Double> trend = swarm.getCostTrend();
logger.info("trend: {}", trend);
```

To visualize the performance of the bees swarm algorithm over time:

```java
CostTrend chart = new CostTrend(trend, "Cost vs Generation");
chart.showIt(true);
```

### Particle Swarm Optimization

The sample code below shows how to use the PSO algorithm to solve the Rosenbrock minimization problem:

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
```

To visualize the performance of the particle swarm algorithm over time:

```java
CostTrend chart = new CostTrend(trend, "Cost vs Generation");
chart.showIt(true);
```

### Ant System

The sample code below shows how to solve a TSP (Travelling Salesman Problem) instance using Ant System:
 
 ```java
 // load the bayg29 TSP instance
 TspBenchmark benchmark = Tsp.get(Tsp.Instance.bayg29);
 
PathCostFunction costFunction = new PathCostFunction() {
  // compute the cost of the tour constructed by an ant on the problem bayg29
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

System.out.println("minimal total distance found by Ant System: " + bestAnt.getCost());
System.out.println("known minimal total distance: " + costFunction.evaluate(benchmark.optTour()));

System.out.println("best TSP path found: ");
for(int i=0; i < bestAnt.getPath().size(); ++i) {
  int j = (i + 1) % bestAnt.getPath().size();
  System.out.println(bestAnt.getPath().get(i) + " => " + bestAnt.getPath().get(j));
}
```

To visualize the performance of the ant system algorithm over time:

```java
CostTrend chart = new CostTrend(antSystem.getCostTrend(), "Cost vs Generation");
chart.showIt(true);
```
 
 ### Ant Colony System
 
 The sample code below shows how to solve a TSP (Travelling Salesman Problem) instance using Ant Colony System:
  
```java
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



AntColonySystem antColonySystem = new AntColonySystem();
antColonySystem.setProblemSize(benchmark.size());
antColonySystem.setCostFunction(costFunction);

antColonySystem.setMaxIterations(100);

Ant bestAnt = antColonySystem.solve();

System.out.println("minimal total distance found: " + bestAnt.getCost());
System.out.println("best known cost: " + costFunction.evaluate(benchmark.optTour()));

System.out.println("best path found: ");
for(int i=0; i < bestAnt.getPath().size(); ++i) {
  int j = (i + 1) % bestAnt.getPath().size();
  System.out.println(bestAnt.getPath().get(i) + " => " + bestAnt.getPath().get(j));
}
```

To visualize the performance of the ant colony system algorithm over time:

```java
CostTrend chart = new CostTrend(antColonySystem.getCostTrend(), "Cost vs Generation");
chart.showIt(true);
```


