package com.staboss.spark.gradient_descent;

import com.staboss.spark.gradient_descent.data.DataPoint;
import com.staboss.spark.gradient_descent.data.ExperienceData;
import com.staboss.spark.gradient_descent.math.function.GradientFunction;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GradientFunctionTest {

    private JavaSparkContext context;
    private static final double STEP = 1e-1;
    private static final double DELTA = 5.0;

    @Before
    public void init() throws IllegalArgumentException {
        Logger.getLogger("org.apache").setLevel(Level.WARN);
        SparkConf conf = new SparkConf();
        conf.set("spark.driver.allowMultipleContexts", "true");
        conf.setMaster("local[*]");
        conf.setAppName("junit");
        context = new JavaSparkContext(conf);
    }

    @Test
    public void trainingTest() {
        final List<DataPoint> points = new ArrayList<>();

        double[] x1 = {90.0, 8100.0};
        double[] x2 = {50.0, 2500.0};
        double[] x3 = {70.0, 4900.0};

        points.add(new DataPoint(x1, 249.0));
        points.add(new DataPoint(x2, 169.0));
        points.add(new DataPoint(x3, 206.0));

        JavaRDD<DataPoint> rdd = context.parallelize(points).cache();
        ExperienceData experienceData = GradientFunction.train(rdd, STEP, 100);

        assertEquals(249.0, GradientFunction.getHypothetical(x1, experienceData), DELTA);
        context.stop();
    }
}
