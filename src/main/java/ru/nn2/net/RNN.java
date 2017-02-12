package ru.nn2.net;

import java.util.ArrayList;

/**
 * Created by arnis on 04.09.2016.
 */
public class RNN extends NeuronNet {
        public RNN() {
            setIteration(0);
            neuronLayers = new ArrayList<>();
        }

    @Override
    void calculateInOut() {
//        if (!isTraining()) {
//            boolean makeNull;
//            for (int i = 0; i < neuronLayers.size() - 1; i++) {
//                makeNull = true;
//                for (int j = 0; j < neuronLayers.get(i).size(); j++) {
//                    Neural neuron = neuronLayers.get(i).get(j);
//                    ArrayList<Synapse> synapses = neuron.getLinks();
//                    for (Synapse synapse : synapses) {
//                        Neural linkedNeuron = synapse.getLinkedNeuron();
//                        if (!(linkedNeuron instanceof ContextNeuron)) {
//                            if (makeNull)
//                                linkedNeuron.setInputValue(0);
//                            linkedNeuron.updateInputValue(synapse.getWeight() * neuron.getOutputValue());
//                        }
//                    }
//                    makeNull = false;
//                }
//                if (i != neuronLayers.size() - 1)
//                    calculateOutputs(neuronLayers.get(i + 1));
//            }
//        } else
            super.calculateInOut();
    }
}
