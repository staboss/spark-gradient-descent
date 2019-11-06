package com.staboss.spark.gradient_descent.math.comparator;

import com.staboss.spark.gradient_descent.data.DataPoint;

import java.io.Serializable;
import java.util.Comparator;

public class ComparatorY implements Serializable, Comparator<DataPoint> {
    @Override
    public int compare(DataPoint a, DataPoint b) {
        return Double.compare(a.getY(), b.getY());
    }
}
