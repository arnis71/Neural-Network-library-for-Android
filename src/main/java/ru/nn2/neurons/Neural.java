package ru.nn2.neurons;


import java.util.ArrayList;

import ru.nn2.net.ActivationFunction;


/**
 * Created by arnis on 04.09.2016.
 */
public abstract class Neural {
    private double inputValue;
    private double outputValue;
    private double nodeDelta;
    ArrayList<Synapse> links;
    private static ActivationFunction activationFunc;

    public static void setActivationFunction(String function) {
        switch (function){
            case ActivationFunction.SIGMOID: activationFunc = new ActivationFunction.Sigmoid();break;
            case ActivationFunction.HYPERBOLIC_TANGENT: activationFunc = new ActivationFunction.HyperTangent();break;
            case ActivationFunction.RELU: activationFunc = new ActivationFunction.ReLU();break;
            default: activationFunc =null;
        }
    }
    double activationFunction(double x){
        return activationFunc.calculate(x);
    }

    double derivativeActivationFunction(double x){
        return activationFunc.calculateDerivative(x);
    }

    public void calculateOut(){
        this.setOutputValue(activationFunction(this.getInputValue()));
    }

    protected void calculateIn(Neural neuron, Synapse synapse){
        this.updateInputValue(neuron.getInputValue()*synapse.getWeight());
    }

    public void calculateNodeDelta(){
        throw new UnsupportedOperationException("Can not calculate node delta");
    }

    public void linkWithLayer(ArrayList<Neural> layer){
        boolean linked=false;
        for (Neural neuron: layer) {
            Synapse synapse = new Synapse(this);
            neuron.links.add(synapse);
            if (neuron instanceof ContextNeuron&&!linked&&!((ContextNeuron)neuron).isLinked()){
                linked=true;
                ((ContextNeuron) neuron).setLinked(linked);
                Synapse synapseRev = new Synapse(neuron);
                synapseRev.setNoWeight();
                this.getLinks().add(synapseRev);
            }
//            calculateIn(neuron,synapse);
        }
    }

    public ArrayList<Synapse> getLinks() {
        return links;
    }

    public double getInputValue() {
        return inputValue;
    }

    public void setInputValue(double inputValue) {
        this.inputValue = inputValue;
    }

    public void updateInputValue(double inputValue) {
        this.inputValue += inputValue;
    }

    public double getOutputValue() {
        return outputValue;
    }

    public void setOutputValue(double outputValue) {
        this.outputValue = outputValue;
    }

    double getNodeDelta() {
        return nodeDelta;
    }

    void setNodeDelta(double nodeDelta) {
        this.nodeDelta = nodeDelta;
    }


}
