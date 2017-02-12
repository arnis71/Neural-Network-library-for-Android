package ru.nn2;


import ru.nn2.net.ActivationFunction;
import ru.nn2.net.NetDimen;
import ru.nn2.net.NeuronNet;
import ru.nn2.components.TrainingSet;

public class TestClass {
    public static void main(String[] args) {
        int[] hiddenNeurons = new int[]{10};

        NetDimen dimensions = new NetDimen(10,1,hiddenNeurons);

        NeuronNet.Builder builder = new NeuronNet.Builder(NeuronNet.getNN(NeuronNet.FEEDFORWARD_NN));
        builder.setDimensions(dimensions)
                .setBias(true)
                .setErrorCalculation(NeuronNet.MSE)
                .setActivationFunction(ActivationFunction.SIGMOID);

        NeuronNet net = builder.build();
        net.setMaxIterations(50000);

        TrainingSet trainingSet = new TrainingSet();// film year,total $, country (usa,france,russia)
        trainingSet.addEntry(new TrainingSet.Set(new double[]{1,0,0,0,0, 0,0,0,1,0}, new double[]{1}));
        trainingSet.addEntry(new TrainingSet.Set(new double[]{0,1,0,0,0, 0,0,0,0,1}, new double[]{0}));
        trainingSet.addEntry(new TrainingSet.Set(new double[]{0,0,1,0,0, 0,0,1,0,0}, new double[]{0.8}));
        trainingSet.addEntry(new TrainingSet.Set(new double[]{0,0,0,1,0, 0,1,0,0,0}, new double[]{0.5}));
        trainingSet.addEntry(new TrainingSet.Set(new double[]{0,0,0,0,1, 1,0,0,0,0}, new double[]{0.3}));

//        trainingSet.addEntry(new TrainingSet.Set(new double[]{0,0},new double[]{0}));
//        trainingSet.addEntry(new TrainingSet.Set(new double[]{0,1},new double[]{1}));
//        trainingSet.addEntry(new TrainingSet.Set(new double[]{1,0},new double[]{1}));
//        trainingSet.addEntry(new TrainingSet.Set(new double[]{1,1},new double[]{0}));

        net.setTrainingSet(trainingSet);

        net.start();

        net.join();
        trainingSet = new TrainingSet();
        trainingSet.addEntry(new TrainingSet.Set(new double[]{0,1,0,0,0, 1,0,0,0,0},new double[]{}));
        net.setTrainingSet(trainingSet);
        net.setMode(NeuronNet.WORKING_MODE);
        net.start();
    }
}
