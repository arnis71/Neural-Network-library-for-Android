package ru.nn2.net;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;

import ru.nn2.neurons.Neural;
import ru.nn2.neurons.Synapse;


/**
 * Created by arnis on 22/09/2016.
 */

class Brains {
    private ArrayList<ArrayList<Double>> matrices;
    private SharedPreferences brainsStorage;
    private SharedPreferences dimenStorage;
    private Context context;
    private int sch = 1;

    Brains(Context context) {
        this.context = context;
        matrices = new ArrayList<>();
    }

    boolean checkCompat(String name,NeuronNet net){
        if (name.equals("default"))
            return false;

        dimenStorage = context.getSharedPreferences(name+"_storage",Context.MODE_PRIVATE);

        if (net.neuronLayers.size()!=dimenStorage.getInt("total",0))
            return false;
        if (net.neuronLayers.get(0).size()!=dimenStorage.getInt("input",0))
            return false;
        if (net.neuronLayers.get(net.neuronLayers.size()-1).size()!=dimenStorage.getInt("output",0))
            return false;
        for (int i = 1; i < net.neuronLayers.size()-1; i++) {
            if (net.neuronLayers.get(i).size()!=dimenStorage.getInt("hidden"+Integer.toString(i),0))
                return false;
        }

        return true;
    }

    void saveWeights(String name, NeuronNet neuronNet){
        Log.d("happy", "saveWeights: ");
        brainsStorage = context.getSharedPreferences(name+"_brains", Context.MODE_PRIVATE);
        brainsStorage.edit().clear().apply();
        matrices.clear();
        for (int i = 0; i < neuronNet.neuronLayers.size() - 1; i++) {
            for (int j = 0; j < neuronNet.neuronLayers.get(i).size(); j++) {
                matrices.add(getWeights(neuronNet.neuronLayers.get(i).get(j)));
            }
        }
        Log.d("happy", "matricies size "+Integer.toString(matrices.size()));

        saveDimens(name, neuronNet);
        sch = 1;
    }

    private void saveDimens(String name,NeuronNet neuronNet) {
        dimenStorage = context.getSharedPreferences(name+"_storage",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = dimenStorage.edit().clear();
        editor.putInt("total",neuronNet.neuronLayers.size());
        editor.putInt("input",neuronNet.neuronLayers.get(0).size());
        editor.putInt("output",neuronNet.neuronLayers.get(neuronNet.neuronLayers.size()-1).size());
        for (int i = 1; i < neuronNet.neuronLayers.size()-1; i++) {
            editor.putInt("hidden"+Integer.toString(i),neuronNet.neuronLayers.get(i).size());
        }
        editor.apply();
    }

    void loadBrains(String name,NeuronNet neuronNet){
        brainsStorage = context.getSharedPreferences(name+"_brains",Context.MODE_PRIVATE);
        if (matrices.size()==0) {
            matrices.add(new ArrayList<Double>());
            int i=0;
            for (sch=1; sch<= brainsStorage.getAll().size()+i; sch++){
                if (!brainsStorage.contains(Integer.toString(sch))){
                    matrices.add(new ArrayList<Double>());
                    i++;
                } else matrices.get(i).add(Double.parseDouble(brainsStorage.getString(Integer.toString(sch),"0")));
            }
            sch=1;
        }

        int u=0;
        for (int i = 0; i < neuronNet.neuronLayers.size() - 1; i++) {
            for (int k = 0; k < neuronNet.neuronLayers.get(i).size(); k++) {
                for (int j = 0; j < neuronNet.neuronLayers.get(i).get(k).getLinks().size(); j++) {
//                    try {
                        neuronNet.neuronLayers.get(i).get(k).getLinks().get(j).setWeight(matrices.get(u).get(j));
//                    } catch (IndexOutOfBoundsException e) {
//                        e.printStackTrace();
//                    }
                }
                u++;
            }
        }

    }

    private ArrayList<Double> getWeights(Neural neural){
        ArrayList<Double> arr = new ArrayList<>();
        for (Synapse synapse:neural.getLinks()){
            double d = synapse.getWeight();
            arr.add(d);
            brainsStorage.edit().putString(Integer.toString(sch++),Double.toString(d)).apply();
        }
        sch++;

        return arr;
    }

    public void reset(String name) {
        context.getSharedPreferences(name +"_storage",Context.MODE_PRIVATE).edit().clear().apply();
        context.getSharedPreferences(name +"_brains", Context.MODE_PRIVATE).edit().clear().apply();
        matrices.clear();
    }
}
