package com.staboss.spark.gradient_descent.math.comparator;

import com.staboss.spark.gradient_descent.data.DataPoint;

import java.io.Serializable;
import java.util.Comparator;

public class ComparatorX implements Serializable, Comparator<DataPoint> {

    private int i;

    public ComparatorX(int i) {
        this.i = i;
    }

    @Override
    public int compare(DataPoint a, DataPoint b) {
        return Double.compare(a.getX()[i], b.getX()[i]);
    }
}
