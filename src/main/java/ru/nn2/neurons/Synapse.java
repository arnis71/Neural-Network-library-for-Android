package ru.nn2.neurons;

import java.util.Random;

/**
 * Created by arnis on 04.09.2016.
 */
public class Synapse {
    private static Random rnd = new Random();

    private boolean noWeight;
    private double weight;
    private double previousWeightChange=0;
    private double gradient;
    private Neural linkedNeuron;


    Synapse(Neural linkedNeuron) {
        this.linkedNeuron = linkedNeuron;
        weight = rnd.nextDouble();
        noWeight=false;
    }

    public void calculateGradient(Neural neuron){
        setGradient(linkedNeuron.getNodeDelta()*neuron.getOutputValue());
    }

    public double getPreviousWeightChange() {
        return previousWeightChange;
    }

    public void setPreviousWeightChange(double previousWeightChange) {
        this.previousWeightChange = previousWeightChange;
    }
    public void setWeight(double weight) {
        if (!noWeight)
            this.weight = weight;
    }

    void setNoWeight(){
        noWeight=true;
        weight=1;
    }

    public double getWeight() {
        return weight;
    }

    public void updateWeight() {
        if (!noWeight)
            this.weight += previousWeightChange;
    }

    public double getGradient() {
        return gradient;
    }

    void setGradient(double gradient) {
        this.gradient = gradient;
    }

    public Neural getLinkedNeuron() {
        return linkedNeuron;
    }

}
