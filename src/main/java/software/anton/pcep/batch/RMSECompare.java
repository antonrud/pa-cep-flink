package software.anton.pcep.batch;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class RMSECompare {

  private static final int MAX_NODES = 100;

  public static void main(String[] args) throws Exception {

    String PATH_ACTUAL =       "src/main/resources/weekly/week2/predict.txt";
    String PATH_PREDITTED_30 = "src/main/resources/weekly/week2/result30.txt";
    String PATH_PREDITTED_1 =  "src/main/resources/weekly/week2/result1.txt";

    List<Double> actual30 = Files.lines(Paths.get(PATH_ACTUAL))
            .skip(6)
            .limit(329)
            .map(Double::parseDouble)
            .collect(Collectors.toList());

    List<Double> actual1 = Files.lines(Paths.get(PATH_ACTUAL))
            .skip(7)
            .limit(329)
            .map(Double::parseDouble)
            .collect(Collectors.toList());

    List<Double> predicted30 = Files.lines(Paths.get(PATH_PREDITTED_30))
            .map(Double::parseDouble)
            .collect(Collectors.toList());

    List<Double> predicted1 = Files.lines(Paths.get(PATH_PREDITTED_1))
            .map(Double::parseDouble)
            .collect(Collectors.toList());

    double sum = 0;
    for (int i = 0; i < actual30.size(); i++) {
      sum += Math.pow(predicted30.get(i) - actual30.get(i), 2);
    }
    System.out.println(Math.sqrt(sum / actual30.size()));

    sum = 0;
    for (int i = 0; i < actual1.size(); i++) {
      sum += Math.pow(predicted1.get(i) - actual1.get(i), 2);
    }
    System.out.println(Math.sqrt(sum / actual1.size()));
  }
}
