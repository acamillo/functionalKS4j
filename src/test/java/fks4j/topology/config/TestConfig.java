package fks4j.topology.config;

import fks4j.example.Configuration;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import org.apache.kafka.streams.StreamsConfig;

public class TestConfig {

  public final static Map<String, String> kafkaMapProps = Map.of(
      StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9091",
      StreamsConfig.APPLICATION_ID_CONFIG, UUID.randomUUID().toString(),
      StreamsConfig.STATE_DIR_CONFIG, "/tmp/tests/" + UUID.randomUUID()
  );
  public final static Map<String, String> configProps = Map.of(
      "INPUT_TOPIC_ONE", "inputModel1",
      "INPUT_TOPIC_TWO", "inputModel2",
      "OUTPUT_TOPIC_ONE", "outputModel3",
      "OUTPUT_TOPIC_TWO", "outputModel1",
      "OUTPUT_TOPIC_M2", "outputModel2",
      "JOIN_DURATION_SEC", "60"
  );

  public static String string(String name) {
    return configProps.get(name);
  }

  public static Duration duration(String name, TemporalUnit unit) {
    return Duration.of(Integer.parseInt(configProps.get(name)), unit);
  }
  public static Configuration build() {
    var kProps = new Properties();
    kProps.putAll(kafkaMapProps);

    return new Configuration(
        string("INPUT_TOPIC_ONE"),
        string("INPUT_TOPIC_TWO"),
        string("OUTPUT_TOPIC_ONE"),
        string("OUTPUT_TOPIC_TWO"),
        string("OUTPUT_TOPIC_M2"),
        duration("JOIN_DURATION_SEC", ChronoUnit.SECONDS),
        kProps);
  }

}
