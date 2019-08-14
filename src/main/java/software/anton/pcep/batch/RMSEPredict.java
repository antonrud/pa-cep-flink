package software.anton.pcep.batch;

import smile.regression.RegressionTree;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RMSEPredict {

  public static void main(String[] args) throws Exception {

    String WEEK = "15";

    List<Double> trainList = Files.lines(Paths.get("src/main/resources/weekly/week" + WEEK + "/train.txt"))
            .map(Double::parseDouble)
            .collect(Collectors.toList());

    List<Double> predictList = Files.lines(Paths.get("src/main/resources/weekly/week" + WEEK + "/predict.txt"))
            .map(Double::parseDouble)
            .collect(Collectors.toList());


    double[][] trainArr = createExplValues(trainList);
    double[][] predictArr = createExplValues(predictList);
    double[] trainArray30m = createPredictArray30m(trainList);
    double[] trainArray1h = createPredictArray1h(trainList);

    RegressionTree model30m = new RegressionTree(trainArr, trainArray30m, 100);
    RegressionTree model1h = new RegressionTree(trainArr, trainArray1h, 100);

    for (double[] values : predictArr) {
      System.out.println(model30m.predict(values));
    }

    System.out.println("\n\n\n\n\n");


    for (double[] values : predictArr) {
      System.out.println(model1h.predict(values));
    }
  }

  private static double[] createPredictArray30m(List<Double> trainList) {

    double[] predictArr = new double[trainList.size() - 7];

    for (int i = 6; i < trainList.size() - 1; i++) {
      predictArr[i - 6] = trainList.get(i);
    }

    return predictArr;
  }

  private static double[] createPredictArray1h(List<Double> trainList) {

    double[] predictArr = new double[trainList.size() - 7];

    for (int i = 7; i < trainList.size(); i++) {
      predictArr[i - 7] = trainList.get(i);
    }

    return predictArr;
  }

  private static double[][] createExplValues(List<Double> trainList) {

    int count = 0;
    double[][] trainArr = new double[trainList.size() - 7][6];

    for (int i = 0; i < trainList.size() - 7; i++) {

      double[] set = new double[6];
      for (int j = 0; j < 6; j++) {
        set[j] = trainList.get(count + j);
      }

      trainArr[i] = calculateExplanatoryVariables(set);
      count++;
    }

    return trainArr;
  }

  static double[] calculateExplanatoryVariables(double[] variables) {

    double sum = Arrays.stream(variables).sum();
    double max = Arrays.stream(variables).max().getAsDouble();
    double min = Arrays.stream(variables).min().getAsDouble();
    double avg = Arrays.stream(variables).average().getAsDouble();
    double diff = variables[5] - variables[0];

    Arrays.sort(variables);
    double median = (variables[variables.length / 2] + variables[variables.length / 2 - 1]) / 2;

    return new double[]{median, sum, max, min, avg, diff};
  }
}
