package ru.nn2.net;

/**
 * Created by arnis on 05.09.2016.
 */
public abstract class Error {
    private double err;

    double getValue() {
        return err;
    }
    void setValue(double err) {
        this.err = err;
    }
    void updateValue(double err){
        this.err+=err;
    }

    abstract double calculate(int outputs, double totalError);

    static class MeanSquaredError extends Error{

        @Override
        public double calculate(int outputs,double totalError) {
            return totalError/outputs;
        }

        public double squareError(double[] ideal,double[] actual){
            double sum=0;
            for (int i = 0; i < actual.length; i++) {
                sum+=Math.pow(ideal[i]-actual[i],2);
            }
            return sum/actual.length;
        }
    }

    static class SimpleError extends Error{

        @Override
        public double calculate(int outputs, double totalError) {
            return totalError;
        }
        public double absError(double[] ideal,double[] actual){
            double sum=0;
            for (int i = 0; i < actual.length; i++) {
                sum+=Math.abs(ideal[i]-actual[i]);
            }
            return sum/actual.length;
        }
    }

    static class ArctanError extends Error{

        @Override
        public double calculate(int outputs,double totalError) {
            return 0;
        }
    }
}
