package ru.nn2.net;

/**
 * Created by arnis on 05.09.2016.
 */
public class NetDimen {
    public int getTotalLayers() {
        return 2+hiddenLayersNeurons.length;
    }
    public int getInputNeurons() {
        return inputNeurons;
    }
    public void setInputNeurons(int inputNeurons) {
        this.inputNeurons = inputNeurons;
    }
    public int getOutputNeurons() {
        return outputNeurons;
    }
    public void setOutputNeurons(int outputNeurons) {
        this.outputNeurons = outputNeurons;
    }
    public int getHiddenLayersNeuron(int index) {
        return hiddenLayersNeurons[index];
    }

    private int inputNeurons;
    private int outputNeurons;
    private int[] hiddenLayersNeurons;

    public NetDimen(int inputNeurons, int outputNeurons, int[] hiddenLayersNeurons) {
        this.inputNeurons = inputNeurons;
        this.outputNeurons = outputNeurons;
        this.hiddenLayersNeurons = hiddenLayersNeurons;
    }
}
