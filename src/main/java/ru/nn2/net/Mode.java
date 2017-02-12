package ru.nn2.net;

/**
 * Created by arnis on 06.09.2016.
 */
interface Mode {
    void start(NeuronNet neuronNet);

    class Learning implements Mode{
        @Override
        public void start(NeuronNet neuronNet) {
            System.out.println("TRAINING");
            neuronNet.train();
        }
    }

    class Validation implements Mode{
        @Override
        public void start(NeuronNet neuronNet) {
            System.out.println("VALIDATION");
        }
    }
    class Working implements Mode{
        @Override
        public void start(NeuronNet neuronNet) {
            System.out.println("WORKING");
            neuronNet.loadValuesFromSet(0);
            neuronNet.calculateInOut();
            neuronNet.getInfo();
        }
    }
}
