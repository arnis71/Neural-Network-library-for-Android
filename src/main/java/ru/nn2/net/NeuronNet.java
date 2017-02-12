package ru.nn2.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;

import ru.nn2.components.OnCompleteListener;
import ru.nn2.components.TrainingSet;
import ru.nn2.neurons.InputNeuron;
import ru.nn2.neurons.Neural;
import ru.nn2.neurons.OutputNeuron;
import ru.nn2.neurons.Synapse;


/**
 * Created by arnis on 04.09.2016.
 */
public abstract class NeuronNet {
    public static final String FEEDFORWARD_NN = "feedforward";
    public static final String RNN = "elman";

    public static final String BACKPROPAGATION_TRAINING = "backpropagation";

    public static final String MSE = "mse";
    public static final String SIMPLE_ERR = "simple";
    public static final String ARCTAN_ERROR = "arctan";

    public static final String TRAINING_MODE = "training";
    public static final String VALIDATION_MODE = "validation";
    public static final String WORKING_MODE = "working";

    private static final String BRAINS_STORAGE = "brainstorage";

    private String name;
    private double learningRate = 0.3;
    private double momentum = 0.7;
    private Brains brains;
    private Mode mode;
    private Training training;
    private TrainingSet trainingSet;
    private Error error;
    private Thread neuralThread;
    private int epoch;
    private int iteration;
    private int maxIterations;
    private double forceStop;
    ArrayList<ArrayList<Neural>> neuronLayers;
//    public static NeuronNet requestStockSolvingNN(Context context, Prefs prefs){
//
//        int[] hiddenNeurons = new int[]{5,5};
//
//        NetDimen dimensions = new NetDimen(prefs.getWindow(),prefs.getPrediction(),hiddenNeurons);
//
//        NeuronNet.Builder builder = new NeuronNet.Builder(NeuronNet.getNN(prefs.getTypeNN()));
//        builder.setDimensions(dimensions)
//                .setBias(true)
//                .setErrorCalculation(prefs.getErrorCalculation())
//                .setActivationFunction(ActivationFunction.HYPERBOLIC_TANGENT)
//                .addBrains(context);
//
//        NeuronNet net = builder.build();
//        net.setMaxIterations(prefs.getIterations());
//
//        net.epoch = context.getSharedPreferences(prefs.getNeuralName()+"_info",Context.MODE_PRIVATE).getInt("epoch",0);
//        net.err = Double.parseDouble(context.getSharedPreferences(prefs.getNeuralName()+"_info",Context.MODE_PRIVATE).getString("error","0"));
//
//        if (prefs.isTrain())
//            net.setMode(TRAINING_MODE);
//        else net.setMode(WORKING_MODE);
//
//        net.loadBrains(prefs.getNeuralName());
//
//        net.setName(prefs.getNeuralName());
//
//        return net;
//    }

//    public void addStockData(List<Stock> data){
//        TrainingSet trainingSet = new TrainingSet();
//
//        if (isTraining())
//            trainingSet.addTrainStocks(data,neuronLayers.get(0).size()-1,neuronLayers.get(neuronLayers.size()-1).size());
//        else trainingSet.addWorkStocks(data,neuronLayers.get(0).size()-1);
//
//        setTrainingSet(trainingSet);
//    }
//    public void addCurrencyData(List<Currency> data){
//        TrainingSet trainingSet = new TrainingSet();
//
//        if (isTraining())
//            trainingSet.addTrainCurrency(data,neuronLayers.get(0).size()-1,neuronLayers.get(neuronLayers.size()-1).size());
//        else trainingSet.addWorkCurrency(data,neuronLayers.get(0).size()-1);
//
//        setTrainingSet(trainingSet);
//    }

    private OnCompleteListener onCompleteListener;

    private void setTrainingMode(String training){
        switch (training){
            case BACKPROPAGATION_TRAINING: this.training= new Training.BackPropagation(this);break;
            default: this.training = null;
        }
    }
    private void setActivationFunc(String activFunc){
        Neural.setActivationFunction(activFunc);
    }

    private void setErrorCalc(String type){
        switch (type){
            case MSE: error = new Error.MeanSquaredError();break;
            case ARCTAN_ERROR: error = new Error.ArctanError();break;
            case SIMPLE_ERR: error = new Error.SimpleError();break;
            default:throw new IllegalArgumentException("No such error calculation: "+type);
        }
    }
    void addError(double[] idealOut, double[] actualOut){
        if (error instanceof Error.MeanSquaredError)
            error.updateValue(((Error.MeanSquaredError)error).squareError(idealOut,actualOut));
        else if (error instanceof Error.SimpleError){
            error.updateValue(((Error.SimpleError)error).absError(idealOut,actualOut));
        }
    }
    void calculateError(boolean print){
        error.setValue(error.calculate(trainingSet.getSetEntries(),error.getValue()));
        if (print)
            Log.d("happynn", "ERROR: " + String.format("%.12f",error.getValue()*100)+"%");
    }
    void resetErr(){
        error.setValue(0);
    }
    public double getTotalError(){
        return error.getValue();
    }
    public void setError(double val) {
        this.error.setValue(val);
    }
    boolean isForceStop() {
        return forceStop != 0 && getTotalError()!=0 &&getTotalError()*100 < forceStop;
    }

    public void forceStopAtError(double value){
        forceStop=value;

    }

    void calculateOutputs(ArrayList<Neural> neurals){
        for (Neural neuron:neurals){
            neuron.calculateOut();
        }
    }
    void calculateGradientsUpdateWeights(ArrayList<Neural> neurals){
        for (Neural neuron:neurals) {
            ArrayList<Synapse> synapses = neuron.getLinks();
            for (Synapse synapse : synapses) {
                synapse.calculateGradient(neuron);
                synapse.setPreviousWeightChange((getLearningRate() * synapse.getGradient() + (synapse.getPreviousWeightChange() * getMomentum())));
                synapse.updateWeight();
            }
        }
    }
    void calculateInOut(){
        boolean makeNull;
        for (int i = 0; i < neuronLayers.size()-1; i++) {
            makeNull=true;
            for (int j = 0; j < neuronLayers.get(i).size(); j++) {
                Neural neuron = neuronLayers.get(i).get(j);
                ArrayList<Synapse> synapses = neuron.getLinks();
                for (Synapse synapse:synapses){
                    if (makeNull)
                        synapse.getLinkedNeuron().setInputValue(0);
                    synapse.getLinkedNeuron().updateInputValue(synapse.getWeight()*neuron.getOutputValue());
                }
                makeNull=false;
            }
            if (i!=neuronLayers.size()-1)
                calculateOutputs(neuronLayers.get(i+1));
        }
    }
    void loadValuesFromSet(int set){
        changeInputs(getTrainingSet().getEntry(set).getInputValues());
        if (getMode() instanceof Mode.Learning)
            changeIdealOutputs(getTrainingSet().getEntry(set).getDesiredOutput());
    }
    private void changeInputs(double... inputs){
        for (int i = 0; i < inputs.length; i++) {
            neuronLayers.get(0).get(i).setInputValue(inputs[i]);
        }
    }
    private void changeIdealOutputs(double... outputs){
        for (int i = 0; i < outputs.length; i++) {
            ((OutputNeuron)neuronLayers.get(neuronLayers.size()-1).get(i)).setIdealOutputValue(outputs[i]);
        }
    }

    public void setBrains(Brains brains){
        this.brains = brains;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public void setMomentum(double value) {
        momentum = value;
    }
    private double getMomentum() {
        return momentum;
    }
    public void setLearningRate(double value){
        learningRate=value;
    }
    private double getLearningRate(){
        return learningRate;
    }
    public static NeuronNet getNN(String type){
        switch (type){
            case FEEDFORWARD_NN: return new FeedForwardNN();
            case RNN: return new RNN();
            default: return null;
        }
    }
    public boolean isTraining() {
        return training!=null;
    }
    public int getEpoch() {
        return epoch;
    }
    public void setEpoch(int epoch) {
        this.epoch = epoch;
    }
    void setIteration(int iteration) {
        this.iteration = iteration;
    }
    void iterate() {
        iteration++;
        this.epoch++;
    }
    void resetIterations(){
        iteration=0;
    }
    public int getMaxIterations() {
        return maxIterations;
    }
    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }
    int getIteration(){
        return iteration;
    }
    public TrainingSet getTrainingSet() {
        return trainingSet;
    }
    public void setTrainingSet(TrainingSet trainingSet) {
        this.trainingSet = trainingSet;
    }
    public double[] getOutput(){
        double[] out = new double[neuronLayers.get(neuronLayers.size() - 1).size()];
        for (int i = 0; i < this.neuronLayers.get(neuronLayers.size() - 1).size(); i++) {
            out[i]=neuronLayers.get(neuronLayers.size()-1).get(i).getOutputValue();
        }
        return out;
    }
    public Mode getMode() {
        return mode;
    }
    public void setMode(String type) {
        switch (type){
            case TRAINING_MODE: mode = new Mode.Learning(); this.setTrainingMode(BACKPROPAGATION_TRAINING);break;
            case VALIDATION_MODE: mode = new Mode.Validation();break;
            case WORKING_MODE: mode = new Mode.Working(); this.setTrainingMode("");break;
        }
    }

    public void start(){
        final NeuronNet copy = this;
        neuralThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (getTrainingSet()!=null&&getTrainingSet().getSetEntries()>0)
                    getMode().start(copy);
            }
        });
        neuralThread.start();
    }
    public void startWithListener(OnCompleteListener listener){
        final NeuronNet copy = this;
        onCompleteListener = listener;
        if (getTrainingSet()!=null&&getTrainingSet().getSetEntries()>0) {
            neuralThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    getMode().start(copy);
                    if (onCompleteListener!=null)
                        onCompleteListener.onComplete();
                }
            });
            neuralThread.start();
        }
        else if (onCompleteListener!=null)
            onCompleteListener.onComplete();
    }

    public void resetBrains(Context context){
        context.getSharedPreferences(name + "_info", Context.MODE_PRIVATE).edit().clear().apply();
        brains.reset(name);
    }
    public void store(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(name + "_info", Context.MODE_PRIVATE).edit();
        editor.putInt("epoch", getEpoch());
        editor.putString("error", Double.toString(getTotalError())).apply();
        brains.saveWeights(name, this);
        editor = context.getSharedPreferences(BRAINS_STORAGE, Context.MODE_PRIVATE).edit();
        editor.putString(name, name).apply();
    }
    public void loadBrains(){
        if (brains.checkCompat(name,this)) {
            Log.d("happy", "brains fit");
            try {
                brains.loadBrains(name,this);
            } catch (IndexOutOfBoundsException e) {
                Log.d("happy", "brains damaged");
            }
        } else Log.d("happy", "brains do not fit");
    }

    void getInfo(){
        for (int i = 0; i < neuronLayers.get(0).size()-1; i++) {
            Neural neural =neuronLayers.get(0).get(i);
            if (neural instanceof InputNeuron)
                System.out.println("FOR INPUT: " + String.format("%.6f",neural.getInputValue()));
        }
        for (int i = 0; i < neuronLayers.get(neuronLayers.size()-1).size(); i++) {
            System.out.println("IDEAL_OUTPUT: "+ ((OutputNeuron)neuronLayers.get(neuronLayers.size()-1).get(i)).getIdealOutputValue());
            Log.d("happynn", "OUTPUT: "+ String.format("%.4f",neuronLayers.get(neuronLayers.size()-1).get(i).getOutputValue()));
        }
        System.out.println("-----------------------------------");
    }
    void train(){
        training.train();
    }
    public void join(){
        try {
            if (neuralThread!=null)
                neuralThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static class Builder{
        private NeuronNet net;
        private NetDimen dimensions;
        private boolean withBias;

        public Builder(NeuronNet net) {
            this.net = net;
            this.net.error = new Error.MeanSquaredError();
            this.net.setActivationFunc(ActivationFunction.SIGMOID);
            this.net.setMode(TRAINING_MODE);
            this.net.setMaxIterations(5000);
            withBias=true;
            net.setName("default");
        }

        public Builder setDimensions(NetDimen dimensions){
            this.dimensions = dimensions;
            return this;
        }
        public Builder setErrorCalculation(String type) {
            this.net.setErrorCalc(type);
            return this;
        }
        public Builder setActivationFunction(String type){
            this.net.setActivationFunc(type);
            return this;
        }
        public Builder setMode(String type){
            this.net.setMode(type);
            return this;
        }
        public Builder setBias(boolean withBias){
            this.withBias = withBias;
            return this;
        }
        public Builder addBrains(Context context){
            this.net.brains = new Brains(context);
            return this;
        }



        private void addLayers(){
            for (int i = 0; i < dimensions.getTotalLayers(); i++)
                net.neuronLayers.add(new ArrayList<Neural>());
        }

        private void addInputNeurons() {
            ArrayList<Neural> inputLayer = net.neuronLayers.get(0);
            for (int i = 0; i < dimensions.getInputNeurons(); i++){
                inputLayer.add(NeuroFactory.getNeuron(NeuroFactory.INPUT_NEURON));
            }
            addBiasNeuron(0);
            addContextNeurons(inputLayer,dimensions.getHiddenLayersNeuron(0));

        }

        private void addContextNeurons(ArrayList<Neural> layer, int amount) {
            if (net instanceof RNN)
                for (int i = 0; i < amount; i++) {
                    layer.add(NeuroFactory.getNeuron(NeuroFactory.CONTEXT_NEURON));
                }
        }

        private void addHiddenNeurons() {
            int k=0;
            for (int i = 1; i <dimensions.getTotalLayers()-1; i++) {
                for (int j = 0; j < dimensions.getHiddenLayersNeuron(k); j++) {
                    net.neuronLayers.get(i).add(NeuroFactory.getNeuron(NeuroFactory.HIDDEN_NEURON));
                    net.neuronLayers.get(i).get(j).linkWithLayer(net.neuronLayers.get(i - 1));
                }
                k++;
                addBiasNeuron(i);
                if (i<dimensions.getTotalLayers()-2)
                    addContextNeurons(net.neuronLayers.get(i),dimensions.getHiddenLayersNeuron(i));
            }
        }

        private void addOutputNeurons() {
            ArrayList<Neural> outputLayer = net.neuronLayers.get(dimensions.getTotalLayers()-1);
            for (int i = 0; i < dimensions.getOutputNeurons(); i++){
                outputLayer.add(NeuroFactory.getNeuron(NeuroFactory.OUTPUT_NEURON));
                outputLayer.get(i).linkWithLayer(net.neuronLayers.get(dimensions.getTotalLayers()-2));
            }
        }

        private void addBiasNeuron(int i) {
            if (withBias)
                net.neuronLayers.get(i).add(NeuroFactory.getNeuron(NeuroFactory.BIAS_NEURON));
        }

        public NeuronNet build(){
            addLayers();
            addInputNeurons();
            addHiddenNeurons();
            addOutputNeurons();
            return net;
        }
    }
    }


