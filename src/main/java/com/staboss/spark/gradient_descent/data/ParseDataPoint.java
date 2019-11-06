package com.staboss.spark.gradient_descent.data;

import org.apache.spark.api.java.function.Function;

/**
 * Parsing DataPoint
 */
public class ParseDataPoint implements Function<String, DataPoint> {

    @Override
    public DataPoint call(String s) {
        String[] tokens = s.split(" ");

        double y = Double.parseDouble(tokens[0]);
        double[] x = new double[tokens.length - 1];

        for (int i = 0; i < tokens.length - 1; i++) {
            x[i] = Double.parseDouble(tokens[i + 1]);
        }

        return new DataPoint(x, y);
    }
}
