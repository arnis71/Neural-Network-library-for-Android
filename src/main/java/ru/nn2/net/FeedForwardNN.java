package ru.nn2.net;

import java.util.ArrayList;

/**
 * Created by arnis on 04.09.2016.
 */
public class FeedForwardNN extends NeuronNet {

    protected FeedForwardNN() {
        setIteration(0);
        neuronLayers = new ArrayList<>();
    }

}
