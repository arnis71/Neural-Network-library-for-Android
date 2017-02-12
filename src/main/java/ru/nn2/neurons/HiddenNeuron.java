package ru.nn2.neurons;

import java.util.ArrayList;

/**
 * Created by arnis on 04.09.2016.
 */
public class HiddenNeuron extends Neural {

    public HiddenNeuron() {
        this.links = new ArrayList<>();
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
