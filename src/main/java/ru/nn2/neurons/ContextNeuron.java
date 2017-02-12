package ru.nn2.neurons;

import java.util.ArrayList;

/**
 * Created by arnis on 04.09.2016.
 */
public class ContextNeuron extends Neural {


    private boolean linked;

    public ContextNeuron() {
        this.links = new ArrayList<>();
        linked=false;
    }
    public boolean isLinked() {
        return linked;
    }

    public void setLinked(boolean linked) {
        this.linked = linked;
    }

    @Override
    public void updateInputValue(double inputValue) {
        setInputValue(inputValue);
        setOutputValue(inputValue);
    }

    @Override
    public void calculateNodeDelta() {
        if (links.size()!=0) {
            double sum = 0;
            for (Synapse link : links)
                sum += link.getWeight() * link.getLinkedNeuron().getNodeDelta();

            setNodeDelta(sum * derivativeActivationFunction(getOutputValue()));
        }
    }
}
