package com.staboss.spark.gradient_descent.data;

import java.io.Serializable;

/**
 * Dataset item
 */
public class DataPoint implements Serializable {

    private double[] x;
    private double y;
    private double theta;

    public DataPoint(double[] x, double y) {
        this.x = x;
        this.y = y;
        this.theta = 0;
    }

    public double[] getX() {
        return x;
    }

    public void setX(double[] x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }
}
