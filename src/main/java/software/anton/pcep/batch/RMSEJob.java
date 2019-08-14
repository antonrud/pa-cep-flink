package software.anton.pcep.batch;

import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class RMSEJob {

  public static final String PATH = "";

  public static void main(String[] args) throws Exception {

    final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(1);

    DataSource<Integer> stream = env.readTextFile("")

    env.execute("Batch job");
  }
}
