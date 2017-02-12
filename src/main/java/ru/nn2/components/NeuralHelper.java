package ru.nn2.components;//package ru.nn.Net;
//
//import android.app.Activity;
//import android.graphics.Color;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.arnis.neuronnet.Other.OnCompleteListener;
//import com.arnis.neuronnet.Other.Prefs;
//import com.arnis.neuronnet.Other.ValueChangeListener;
//import com.arnis.neuronnet.Retrofit.Currency;
//import com.arnis.neuronnet.Retrofit.Stock;
//import com.github.mikephil.charting.data.Entry;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by arnis on 27/09/2016.
// */
//
//public class NeuralHelper {
//
//    private int complexNets;
//    private int[] windowSet;
//    private int[] predictionSet;
//    private String[] nameSet;
//    private int[] colorSet;
//    int floatingWindow;
//
//    private Activity activity;
//    private List<NeuronNet> nets;
////    private List<NeuronNet> clones;
//
//    public NeuralHelper(Activity activity) {
//        this.activity = activity;
//        nets = new ArrayList<>();
////        clones = new ArrayList<>();
//        complexNets = 5;
//        windowSet = new int[]{2,4,7,10,14};
//        predictionSet = new int[]{1,2,2,3,3};
//        nameSet = new String[]{"ultra short","short","medium","long","ultra long"};
//        colorSet = new int[]{Color.RED,Color.YELLOW,Color.BLUE,Color.GREEN,Color.MAGENTA};
//    }
//
//    public void run(final TextView error){
//        for (final NeuronNet net:nets) {
//            net.startWithListener(new OnCompleteListener() {
//                @Override
//                public void onComplete() {
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            error.setText(String.format("%.4f", net.getTotalError() * 100) + "%");
//                        }
//                    });
//                }
//            });
//        }
//    }
//    public void run(){
////        if (isTraining()){
////            makeClones();
////            for (final NeuronNet net:clones) {
////                net.startWithListener(null);
////            }
////            joinClones();
////            mergeClones();
////        } else
//            for (final NeuronNet net:nets)
//                net.startWithListener(null);
//
//    }
//
//
////    private void makeClones(){
////        for (NeuronNet net:nets)
////            try {
////                clones.add((NeuronNet) net.clone());
////            } catch (CloneNotSupportedException e) {
////                e.printStackTrace();
////            }
////    }
////    private void mergeClones(){
////        nets.clear();
////        for (NeuronNet net:clones)
////            nets.add(net);
////        clones.clear();
////    }
//
//    public void join(){
//        for (NeuronNet net:nets)
//            net.join();
//    }
////    private void joinClones(){
////        for (NeuronNet net:clones)
////            net.join();
////    }
//
//    public void addStockData(List<Stock> stocks){
//        for (NeuronNet net:nets){
//                net.addStockData(stocks);
//        }
//    }
//    public void addCurrencyData(List<Currency> stocks){
//        List<Currency> copy = new ArrayList<>(stocks.size());
//        copy.addAll(stocks);
//
//        for (NeuronNet net:nets){
//            net.addCurrencyData(copy);
//        }
//    }
//
//    public ArrayList<double[]> getPredictions(){
//        ArrayList<double[]> pred = new ArrayList<>();
//        for (NeuronNet net:nets)
//            pred.add(net.getOutput());
//        return pred;
//    }
//
//    public ArrayList<List<Entry>> stockPredictionsToChart(List<Stock> stocks){
//        ArrayList<List<Entry>> res = new ArrayList<>();
//        int k=0;
//        for (NeuronNet net:nets) {
//            double predictFrom = stocks.get(stocks.size()-1).average();
//            double[] arr = net.getOutput();
//            res.add(new ArrayList<Entry>());
//            if (arr.length>0) {
//                double next;
//                int i = stocks.size()-1;
//                res.get(k).add(new Entry((float) i, (float) predictFrom));
//                for (int j = 0; j < arr.length; j++) {
//                    next = predictFrom + (predictFrom * arr[0]);
//                    res.get(k).add(new Entry((float)++i, (float) next));
//                    predictFrom = next;
//                }
//            }
//            k++;
//        }
//        return res;
//    }
//    public ArrayList<List<Entry>> currencyPredictionsToChart(List<Currency> stocks){
//        ArrayList<List<Entry>> res = new ArrayList<>();
//        int k=0;
//        for (NeuronNet net:nets) {
//            double predictFrom = stocks.get(stocks.size()-1).average();
//            double[] arr = net.getOutput();
//            res.add(new ArrayList<Entry>());
//            if (arr.length>0) {
//                double next;
//                int i = stocks.size()-1;
//                res.get(k).add(new Entry((float) i, (float) predictFrom));
//                for (int j = 0; j < arr.length; j++) {
//                    next = predictFrom + (predictFrom * arr[0]);
//                    res.get(k).add(new Entry((float)++i, (float) next));
//                    predictFrom = next;
//                }
//            }
//            k++;
//        }
//        return res;
//    }
//
//    public void addNets(Prefs prefs){
//
//        NeuronNet net;
//        nets.clear();
//        if (prefs.isComplex()){
//            for (int i = 0; i < complexNets; i++) {
//                prefs.setName(prefs.getSymbol()+nameSet[i]);
//                prefs.setWindow(windowSet[i]);
//                prefs.setPrediction(predictionSet[i]);
//                net = NeuronNet.requestStockSolvingNN(activity,prefs);
//                nets.add(net);
//            }
//        } else {
//            net = NeuronNet.requestStockSolvingNN(activity,prefs);
//            nets.add(net);
//        }
//    }
//    public void storeData() {
//        for (NeuronNet net:nets)
//            net.store(activity);
//    }
//    public void setProgressBar(final ProgressBar pb){
//        if (nets.size()==1) {
//            pb.setMax(getNet(0).getMaxIterations());
//            getNet(0).setIterationListener(new ValueChangeListener() {
//                @Override
//                public void onValueChange(final double value) {
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            pb.setProgress((int) value);
//                        }
//                    });
//
//                }
//
//                @Override
//                public void onValueChange(String value) {
//
//                }
//
//                @Override
//                public void onValueChange(double ask, double bid) {
//
//                }
//
//
//                @Override
//                public void onValueChange(ArrayList<Double> values) {
//
//                }
//            });
//        } else{
//            pb.setMax(getLastNet().getMaxIterations());
//            getLastNet().setIterationListener(new ValueChangeListener() {
//                @Override
//                public void onValueChange(final double value) {
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            pb.setProgress((int) value);
//                        }
//                    });
//
//                }
//
//                @Override
//                public void onValueChange(String value) {
//
//                }
//
//                @Override
//                public void onValueChange(double ask, double bid) {
//
//                }
//
//                @Override
//                public void onValueChange(ArrayList<Double> values) {
//
//                }
//            });
//        }
//    }
//
//    public NeuronNet getNet(int index){
//        return nets.get(index);
//    }
//    private NeuronNet getLastNet(){
//        return nets.get(nets.size()-1);
//    }
//    public String getName(int index){
//        return nets.get(index).getName();
//    }
//    public void setName(String name){
//        nets.get(0).setName(name);
//    }
//    public boolean canSaveBrains(){
//        return nets.size() == 1 || !getName(0).equals("default");
//    }
//    public boolean isTraining(){
//        return getNet(0).isTraining();
//    }
//    public int getColor(int index){
//        return colorSet[index];
//    }
//    public void setMode(String trainingMode) {
//        for (NeuronNet net:nets)
//            net.setMode(trainingMode);
//    }
//    public void setFloatingWindow(int value){
//        this.floatingWindow = value;
//    }
//    public int getFloatingWindow(){
//        return floatingWindow;
//    }
//
//}
