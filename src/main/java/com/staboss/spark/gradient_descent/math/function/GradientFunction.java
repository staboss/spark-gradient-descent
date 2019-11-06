package com.staboss.spark.gradient_descent.math.function;

import com.staboss.spark.gradient_descent.math.comparator.ComparatorX;
import com.staboss.spark.gradient_descent.math.comparator.ComparatorY;
import com.staboss.spark.gradient_descent.data.DataPoint;
import com.staboss.spark.gradient_descent.data.ExperienceData;
import com.staboss.spark.gradient_descent.data.MaxMinValue;
import org.apache.spark.api.java.JavaRDD;

import java.util.Arrays;

/**
 * Gradient Descent Function
 *
 * @author Boris Stasenko
 * @see HThetaFunction
 * @see NormalizeFunction
 */
public class GradientFunction {

    public static ExperienceData train(JavaRDD<DataPoint> points, double step, int iterations) {

        double[] weight = new double[points.first().getX().length];
        Arrays.fill(weight, 1);

        long size = points.count();

        //  Calculation of the maximum and minimum values of X
        MaxMinValue maxMinValue = getMaxMin(points, weight.length);

        //  Normalization of X values (from 0 to 1)
        points.foreach(new NormalizeFunction(maxMinValue.getMax(), maxMinValue.getMin()));

        for (int i = 0; i < iterations; i++) {

            //  Calculation of h(theta)
            points.foreach(new HThetaFunction(weight));

            //  Compute new thetas
            for (int j = 0; j < weight.length; j++) {

                int finalJ = j;

                double sum = points.map(dataPoint ->
                        (dataPoint.getTheta() - dataPoint.getY()) * dataPoint.getX()[finalJ])
                        .reduce(Double::sum);

                weight[j] -= (step / size * sum);
            }
        }

        return new ExperienceData(weight, maxMinValue);
    }


    /**
     * Calculation of function value with normalization
     */
    public static double getHypothetical(double[] x, ExperienceData experienceData) {
        double hTheta = 0.0;

        MaxMinValue maxMinValue = experienceData.getMaxMinValue();

        for (int i = 0; i < x.length; i++) {
            hTheta += experienceData.getWeight()[i] * (x[i] - maxMinValue.getMin()[i + 1])
                    / (maxMinValue.getMax()[i + 1] - maxMinValue.getMin()[i + 1]);
        }

        return (hTheta * (maxMinValue.getMax()[0] - maxMinValue.getMin()[0]) + maxMinValue.getMin()[0]);
    }


    /**
     * Calculation of maximum and minimum values in a data set
     */
    private static MaxMinValue getMaxMin(JavaRDD<DataPoint> points, int size) {
        MaxMinValue maxMinValue = new MaxMinValue();

        maxMinValue.setMax(new double[size + 1]);
        maxMinValue.setMin(new double[size + 1]);

        maxMinValue.getMax()[0] = points.max(new ComparatorY()).getY();
        maxMinValue.getMin()[0] = points.min(new ComparatorY()).getY();

        for (int j = 0; j < size; j++) {
            maxMinValue.getMax()[j + 1] = points.max(new ComparatorX(j)).getX()[j];
            maxMinValue.getMin()[j + 1] = points.min(new ComparatorX(j)).getX()[j];
        }

        return maxMinValue;
    }
}
