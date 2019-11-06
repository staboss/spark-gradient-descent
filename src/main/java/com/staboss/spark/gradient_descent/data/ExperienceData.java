package com.staboss.spark.gradient_descent.data;

import java.io.Serializable;

/**
 * Experience DataSet
 */
public class ExperienceData implements Serializable {

    private double[] weight;
    private MaxMinValue maxMinValue;

    public ExperienceData(double[] weight, MaxMinValue maxMinValue) {
        this.weight = weight;
        this.maxMinValue = maxMinValue;
    }

    public double[] getWeight() {
        return weight;
    }

    public void setWeight(double[] weight) {
        this.weight = weight;
    }

    public MaxMinValue getMaxMinValue() {
        return maxMinValue;
    }

    public void setMaxMinValue(MaxMinValue maxMinValue) {
        this.maxMinValue = maxMinValue;
    }
}
