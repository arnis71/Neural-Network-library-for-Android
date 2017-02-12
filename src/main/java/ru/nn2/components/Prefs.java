package ru.nn2.components;

/**
 * Created by arnis on 25/09/2016.
 */

public class Prefs {
    private String brains;
    private String trainingMethod;
    private int iterations;
    private String typeNN;
    private String itemInfo;
    private String errorCalculation;
    private double learningRate;
    private double momentum;

    public Prefs(String brains, String trainingMethod, int iterations, String typeNN, String errorCalculation) {
        this.itemInfo = "default";
        this.brains = brains;
        this.trainingMethod = trainingMethod;
        this.iterations = iterations;
        this.typeNN = typeNN;
        this.errorCalculation = errorCalculation;
    }

    public String getSymbol(){
        return itemInfo;
    }
    public String getTrainingMethod() {
        return trainingMethod;
    }
    public String getNeuralName() {
        return brains;
    }
    public int getIterations() {
        return iterations;
    }
    public String getTypeNN() {
        return typeNN;
    }
    public String getErrorCalculation() {
        return errorCalculation;
    }

}
