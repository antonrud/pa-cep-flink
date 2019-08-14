package software.anton.pcep.batch;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class RCompare {

  public static void main(String[] args) throws Exception {

    String WEEK = "15";

    String PATH_ACTUAL = "src/main/resources/weekly/week" + WEEK + "/predict.txt";
    String PATH_PREDITTED_30 = "src/main/resources/weekly/week" + WEEK + "/result30.txt";
    String PATH_PREDITTED_1 = "src/main/resources/weekly/week" + WEEK + "/result1.txt";

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

    double mean30 = actual30.stream().mapToDouble(Double::doubleValue).sum() / actual30.size();
    double sst = 0;
    for (double x : actual30) {
      sst += (Math.pow(x - mean30, 2));
    }

    double ssr = 0;
    for (double y : predicted30) {
      ssr += (Math.pow(y - mean30, 2));
    }

    System.out.println(ssr / sst);

    double mean1 = actual1.stream().mapToDouble(Double::doubleValue).sum() / actual1.size();
    sst = 0;
    for (double x : actual1) {
      sst += (Math.pow(x - mean30, 2));
    }

    ssr = 0;
    for (double y : predicted1) {
      ssr += (Math.pow(y - mean30, 2));
    }
    System.out.println(ssr / sst);

  }
}
