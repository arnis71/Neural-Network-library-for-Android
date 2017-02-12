package ru.nn2.neurons;

import java.util.ArrayList;

/**
 * Created by arnis on 04.09.2016.
 */
public class BiasNeuron extends Neural {

    private boolean firstAssigned;

    public BiasNeuron() {
        this.setInputValue(1);
        this.setOutputValue(getInputValue());
        this.links = new ArrayList<>();
    }

    @Override
    public void setInputValue(double inputValue) {
        if (!firstAssigned)
            super.setInputValue(inputValue);
    }

    @Override
    public void setOutputValue(double outputValue) {
        if (!firstAssigned){
            super.setOutputValue(outputValue);
            firstAssigned=true;
        }
    }

    @Override
    protected void calculateIn(Neural neuron, Synapse synapse) {

    }

    @Override
    public void calculateOut() {

    }

    @Override
    public void linkWithLayer(ArrayList<Neural> layer) {

    }

    @Override
    public void calculateNodeDelta() {

    }
}
