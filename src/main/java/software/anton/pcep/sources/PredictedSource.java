package software.anton.pcep.sources;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.watermark.Watermark;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Objects;

import static java.util.stream.Collectors.toCollection;
import static software.anton.pcep.configs.Configuration.DATASET;

/**
 * @author Anton Rudacov <anton.rudacov @ gmail.com>
 */
public class PredictedSource implements SourceFunction<String> {

  private boolean isRunning = true;

  @Override
  public void run(SourceContext<String> ctx) throws Exception {

      while (isRunning) {

      }

  }


  @Override
  public void cancel() {
    isRunning = false;
  }
}
