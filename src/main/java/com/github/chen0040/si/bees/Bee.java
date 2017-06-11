package com.github.chen0040.si.bees;


import com.github.chen0040.si.utils.Mediator;
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

    private Serializable constraint;


    public Bee() {

    }

    //local search around rhs
    public Bee dance(Mediator mediator) {
        Bee clone = makeCopy();
        clone.mutate(mediator);
        return clone;
    }



    //factory method
    public Bee makeCopy() {
        Bee clone = new Bee();
        clone.copy(this);
        return clone;
    }

    public void copy(Bee that) {


        this.solution.clear();
        this.solution.addAll(that.solution);


        this.cost = that.cost;
        this.costValid = that.costValid;
    }

    void mutate(Mediator mediator){
        for(int i=0; i < mediator.getDimension(); ++i){
            solution.set(i, mediator.mutateWithinBounds(i, solution.get(i)));
        }
        cost = mediator.evaluate(solution);
        costValid = true;
    }

    public void randomSearch(Mediator mediator) {
        int N = mediator.getDimension();
        solution.clear();
        for(int i=0; i < N; ++i){
            solution.add(mediator.randomWithinBounds(i));
        }
        cost = mediator.evaluate(solution);
        costValid = true;
    }


    public boolean isBetterThan(Bee that) {
        return Double.compare(this.cost, that.cost) < 0;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(int i=0; i< solution.size(); ++i){
            if(i != 0) {
                sb.append(", ");
            }
            sb.append(solution.get(i));
        }
        sb.append("]");
        return sb.toString();
    }
}
