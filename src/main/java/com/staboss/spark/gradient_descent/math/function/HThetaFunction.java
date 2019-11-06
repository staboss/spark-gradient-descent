package com.staboss.spark.gradient_descent.math.function;

import com.staboss.spark.gradient_descent.data.DataPoint;
import org.apache.spark.api.java.function.VoidFunction;

/**
 * H(theta) Function
 *
 * @author Boris Stasenko
 */
public class HThetaFunction implements VoidFunction<DataPoint> {

    private double[] theta;

    public HThetaFunction(double[] theta) {
        this.theta = theta;
    }

    private double point(double[] theta, double[] x) {
        double new_theta = 0;

        for (int i = 0; i < theta.length; i++) {
            new_theta += (theta[i] * x[i]);
        }

        return new_theta;
    }

    @Override
    public void call(DataPoint dataPoint) {

        if (theta.length != dataPoint.getX().length) {
            throw new ArrayIndexOutOfBoundsException("Weight-size is not equal DataPoint-size");
        }

        dataPoint.setTheta(point(theta, dataPoint.getX()));
    }
}
