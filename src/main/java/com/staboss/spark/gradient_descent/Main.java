package com.staboss.spark.gradient_descent;

import com.staboss.spark.gradient_descent.data.DataPoint;
import com.staboss.spark.gradient_descent.data.ExperienceData;
import com.staboss.spark.gradient_descent.data.ParseDataPoint;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.List;

import static com.staboss.spark.gradient_descent.math.function.GradientFunction.getHypothetical;
import static com.staboss.spark.gradient_descent.math.function.GradientFunction.train;
import static java.lang.System.*;

/**
 * Created by Boris Stasenko on 07.11.2019
 */
public class Main {

    public static void main(String[] args) {

        JavaRDD<DataPoint> points;      //  Dataset of data points
        List<DataPoint> originals;      //  List of dataset elements

        double[] weight = null;         //  Neuron weights
        int iterations = 100;           //  Number of iterations
        double step = 1e-1;             //  Step size (the optimal value for work is 0.1)

        //  Check the right number of options
        if (args.length != 4) {
            err.println("USAGE: " + RUN + " [OPTIONS]" + "\n-> " +
                    "To generate data: " + TO_GENERATE + "\n-> " +
                    "To read data from file: " + FROM_FILE);
            exit(1);
        }

        //  Set logger level WARN
        Logger.getLogger("org.apache").setLevel(Level.WARN);

        //  SparkConf object contains information about application
        SparkConf sparkConf = new SparkConf().setAppName("GradientDescent-Spark-App").setMaster("local[*]");

        //  Create a JavaSparkContext object, which tells Spark how to access a cluster
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

        //  Check for dataset generation
        if (args[0].equals("1")) {
            weight = Generator.generateWeight(Integer.parseInt(args[1]));
            points = Generator.generateData(Integer.parseInt(args[2]), weight, sparkContext).cache();
            iterations = Integer.parseInt(args[3]);
        } else {
            step = Double.parseDouble(args[3]);
            iterations = Integer.parseInt(args[2]);
            JavaRDD<String> lines = sparkContext.textFile(args[1]).cache();
            points = lines.map(new ParseDataPoint()).cache();
        }
        originals = points.collect();

        //  Experience dataset
        ExperienceData experienceData = train(points, step, iterations);
        List<DataPoint> collect = points.cache().collect();
        sparkContext.stop();

        //  Printout the results
        print(weight, experienceData, collect, originals);
    }


    /**
     * Printout the results
     */
    private static void print(double[] weight, ExperienceData experienceData, List<DataPoint> points, List<DataPoint> originals) {

        out.println("\n" + DELIMITER);

        if (weight != null) {
            printFormula(weight);
        }

        double minY = experienceData.getMaxMinValue().getMin()[0];
        double maxY = experienceData.getMaxMinValue().getMax()[0];

        for (int i = 0; i < points.size(); i++) {

            double calculatedY = getHypothetical(originals.get(i).getX(), experienceData);
            double denormalizeY = points.get(i).getY() * (maxY - minY) + minY;

            if (weight != null) {

                double expectedY = 0;

                for (double w : weight) {
                    expectedY += w * i;
                }

                out.println(String.format(outputFormat(1), Arrays.toString(originals.get(i).getX()),
                        denormalizeY, calculatedY, expectedY));
            } else {
                out.println(String.format(outputFormat(0), Arrays.toString(originals.get(i).getX()),
                        denormalizeY, calculatedY));
            }
        }
        out.println(DELIMITER);
    }


    /**
     * Printout format
     */
    private static String outputFormat(int flag) {
        if (flag == 1) {
            return "X = %s%nDenormalized Y = %.3f; Calculated Y = %.3f; Expected Y = %.3f%n";
        } else {
            return "X = %s%nDenormalized Y = %.3f; Calculated Y = %.3f%n";
        }
    }


    /**
     * Printout the generated formula
     */
    private static void printFormula(double[] weight) {
        out.println("Generated formula:");
        out.print("Y = ");
        for (int i = 0; i < weight.length; i++) {
            out.print(weight[i] + "*X" + i);
            if (i < weight.length - 1) {
                out.print(" + ");
            }
        }
        out.println("\n");
    }


    private static final String RUN = "spark-submit " +
            "--class com.staboss.spark.gradient_descent.Main " +
            "--master local[*] " +
            "target/GradientDescent-1.0-SNAPSHOT.jar";


    private static final String FROM_FILE = " <flag_generate> <file_path> <number_of_iterations> <step_size>";
    private static final String TO_GENERATE = " <flag_generate> <number_of_x> <number_of_lines> <number_of_iterations>";


    private static final String DELIMITER = "****************************************" +
            "****************************************" + "\n";
}
