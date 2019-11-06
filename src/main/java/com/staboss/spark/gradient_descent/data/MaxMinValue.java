package com.staboss.spark.gradient_descent.data;

import java.io.Serializable;

/**
 * Maximum and minimum values
 */
public class MaxMinValue implements Serializable {

    private double[] max;
    private double[] min;

    public MaxMinValue() {
    }

    public MaxMinValue(double[] max, double[] min) {
        this.max = max;
        this.min = min;
    }

    public double[] getMax() {
        return max;
    }

    public void setMax(double[] max) {
        this.max = max;
    }

    public double[] getMin() {
        return min;
    }

    public void setMin(double[] min) {
        this.min = min;
    }
}
