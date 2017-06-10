package com.github.chen0040.si;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Bee implements Serializable {

    private static final long serialVersionUID = 7670345468729771142L;

    private double cost = 0;
    private boolean costValid = false;

    private final List<Double> solution = new ArrayList<>();
    private final List<Double> upperBounds = new ArrayList<>();
    private final List<Double> lowerBounds = new ArrayList<>();
    private Serializable constraint;


    public Bee() {
    }

    //local search around rhs
    public Bee dance(BeeMediator mediator) {
        Bee clone = makeCopy();
        clone.mutate(mediator);
        return clone;
    }


    public void initialize(BeeMediator mediator) //initialize random solution
    {
        this.lowerBounds.clear();
        this.lowerBounds.addAll(mediator.getLowerBounds());

        this.upperBounds.clear();
        this.upperBounds.addAll(mediator.getUpperBounds());

        this.constraint = mediator.getConstraints();
    }


    //factory method
    public Bee makeCopy() {
        Bee clone = new Bee();
        clone.copy(this);
        return clone;
    }

    public void copy(Bee that) {
        this.lowerBounds.clear();
        this.lowerBounds.addAll(that.lowerBounds);

        this.upperBounds.clear();
        this.upperBounds.addAll(that.upperBounds);

        this.solution.clear();
        this.solution.addAll(that.solution);

        this.constraint = that.constraint;

        this.cost = that.cost;
        this.costValid = that.costValid;
    }

    void mutate(BeeMediator mediator){
        for(int i=0; i < solution.size(); ++i){
            solution.set(i, mediator.mutateWithinBounds(i, solution.get(i)));
        }
        cost = mediator.evaluate(solution, lowerBounds, upperBounds);
        costValid = true;
    }

    public void randomSearch(BeeMediator mediator) {
        int N = lowerBounds.size();
        solution.clear();
        for(int i=0; i < N; ++i){
            solution.add(mediator.randomWithinBounds(i));
        }
        cost = mediator.evaluate(solution, lowerBounds, upperBounds);
        costValid = true;
    }


    public boolean isBetterThan(Bee that) {
        return Double.compare(this.cost, that.cost) < 0;
    }
}
