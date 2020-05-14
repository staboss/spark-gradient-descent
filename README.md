# Implementation of gradient descent in Apache Spark (Java)
## Build project
`mvn package`

## Usage
```
spark-submit --class com.staboss.spark.gradient_descent.Main --master local[*] [JAR-FILE] [OPTIONS]
```

Where `[OPTIONS]` (follow input sequence):
1. Reading from file
    * `<flag_generate>` – 0;
    * `<file_path>` – file path;
    * `<number_of_iterations>` – the number of iterations;
    * `<step_size>` – learning rate.
2. Generate data
    * `<flag_generate>` – 1;
    * `<number_of_x>` – the number of variables X;
    * `<number_of_lines>` – the number of data lines;
    * `<number_of_iterations>` – the number of iterations.

## Example
```
spark-submit --class <path to .Main> --master local[*] GradientDescent-1.0.jar 1 4 4 100
```
