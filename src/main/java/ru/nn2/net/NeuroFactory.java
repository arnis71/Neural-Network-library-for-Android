package ru.nn2.net;


import ru.nn2.neurons.BiasNeuron;
import ru.nn2.neurons.ContextNeuron;
import ru.nn2.neurons.HiddenNeuron;
import ru.nn2.neurons.InputNeuron;
import ru.nn2.neurons.Neural;
import ru.nn2.neurons.OutputNeuron;

/**
 * Created by arnis on 04.09.2016.
 */

class NeuroFactory {
    static final int INPUT_NEURON = 0;
    static final int HIDDEN_NEURON = 1;
    static final int BIAS_NEURON = 2;
    static final int OUTPUT_NEURON = 3;
    static final int CONTEXT_NEURON = 4;

    static Neural getNeuron(int type){
        switch (type){
            case INPUT_NEURON: return new InputNeuron();
            case HIDDEN_NEURON: return new HiddenNeuron();
            case OUTPUT_NEURON: return new OutputNeuron();
            case BIAS_NEURON:return new BiasNeuron();
            case CONTEXT_NEURON:return new ContextNeuron();
            default: throw new IllegalArgumentException("No neuron of type "+type);
        }
    }
}
