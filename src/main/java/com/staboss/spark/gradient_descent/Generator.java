package com.staboss.spark.gradient_descent;

import com.staboss.spark.gradient_descent.data.DataPoint;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Boris Stasenko on 07.11.2019
 */
public class Generator {

    public static double[] generateWeight(int count) {

        Random random = new Random();

        double[] weight = new double[count];
        for (int i = 0; i < weight.length; i++) {
            weight[i] = (double) Math.round((random.nextGaussian() + random.nextInt(10) - 5) * 1000) / 1000;
        }

        if (count > 2) {
            weight[random.nextInt(count)] += 100;
        }

        return weight;
    }

    public static JavaRDD<DataPoint> generateData(int size, double[] weight, JavaSparkContext context) {

        Random random = new Random();

        ArrayList<DataPoint> dataPoints = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            double[] x = new double[weight.length];
            double sum = 0;

            for (int j = 0; j < weight.length; j++) {
                x[j] = i;
                sum += weight[j] * i;
            }

            dataPoints.add(new DataPoint(x, sum + random.nextInt(6) - 3));
        }

        return context.parallelize(dataPoints);
    }
}
