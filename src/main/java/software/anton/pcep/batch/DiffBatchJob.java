package software.anton.pcep.batch;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toCollection;
import static software.anton.pcep.configs.Configuration.DATASET;

public class DiffBatchJob {

  public static void main(String[] args) throws Exception {

    List<Integer> diff = new ArrayList<>();

    List<Integer> linesIn = Files.lines(Paths.get(DATASET))
            .filter(line -> line.charAt(0) == '9')
            .map(x -> Integer.parseInt(x.split(",")[3]))
            .collect(toCollection(LinkedList::new));

    List<Integer> linesOut = Files.lines(Paths.get(DATASET))
            .filter(line -> line.charAt(0) == '7')
            .map(x -> Integer.parseInt(x.split(",")[3]))
            .collect(toCollection(LinkedList::new));

    for (int i = 0; i < linesIn.size(); i++) {
      diff.add(linesIn.get(i) - linesOut.get(i));
    }

    final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(1);

    DataSource<Integer> stream = env.fromCollection(diff);

    stream.writeAsText("src/main/resources/dataset/diffs.txt");

    env.execute("Batch job");
  }
}
