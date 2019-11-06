package com.staboss.spark.gradient_descent;

import com.staboss.spark.gradient_descent.data.DataPoint;
import com.staboss.spark.gradient_descent.math.function.HThetaFunction;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkException;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class HThetaFunctionTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private JavaSparkContext context;

    @Before
    public void init() throws IllegalArgumentException, IOException {
        Logger.getLogger("org.apache").setLevel(Level.WARN);
        SparkConf conf = new SparkConf();
        conf.set("spark.driver.allowMultipleContexts", "true");
        conf.setMaster("local[*]");
        conf.setAppName("junit");
        context = new JavaSparkContext(conf);
    }

    @Test
    public void calculateHThetaTest() {
        final List<DataPoint> points = new ArrayList<>();

        double[] x = {1, 2, 3};
        double[] weight = {1, 1, 1};

        points.add(new DataPoint(x, 1));

        JavaRDD<DataPoint> rdd = context.parallelize(points).cache();
        rdd.foreach(new HThetaFunction(weight));

        assertEquals(6, rdd.collect().get(0).getTheta(), 0);
        context.stop();
    }

    @Test
    public void calculateDifferentSizeTest() {
        final List<DataPoint> points = new ArrayList<>();

        double[] x = {1, 2, 3};
        double[] weight = {1, 1};

        points.add(new DataPoint(x, 1));

        //thrown.expect(ArrayIndexOutOfBoundsException.class);
        thrown.expect(SparkException.class);
        //thrown.expectMessage("Weight size is not equal DataPoint size");

        JavaRDD<DataPoint> rdd = context.parallelize(points).cache();
        rdd.foreach(new HThetaFunction(weight));
        rdd.collect();

        context.stop();
    }
}
