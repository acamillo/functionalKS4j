package fks4j.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import fks4j.kafka.streams.serde.jackson.ConfigurableMapper;
import java.time.Duration;
import java.util.Properties;

public class Configuration implements ConfigurableMapper {

  public final String inputTopic0;
  public final String inputTopic1;
  public final String outputModel3;
  public final String outputModel1;
  public final String outputModel2;

  public final String inputModelD;
  public final String outputModelD;

  public final Duration joinWindow;
  public final Duration aggregationsWindow;
  public final int aggregationThreshold;
  public final Properties properties;
  public final ObjectMapper mapper;


  public Configuration(
      String inputTopic0,
      String inputTopic1,
      String outputModel3,
      String outputModel1,
      String outputModel2,
      String inputModelD,
      String outputModelD,
      Duration joinWindow,
      Duration aggregationsWindow,
      int aggregationThreshold, Properties properties,
      ObjectMapper mapper) {
    this.inputTopic0 = inputTopic0;
    this.inputTopic1 = inputTopic1;
    this.outputModel3 = outputModel3;
    this.outputModel1 = outputModel1;
    this.outputModel2 = outputModel2;
    this.inputModelD = inputModelD;
    this.outputModelD = outputModelD;
    this.aggregationThreshold = aggregationThreshold;
    this.properties = properties;
    this.joinWindow = joinWindow;
    this.aggregationsWindow = aggregationsWindow;
    this.mapper = mapper;
  }

  public Configuration(
      String inputTopic0,
      String inputTopic1,
      String outputModel3,
      String outputModel1,
      String outputModel2,
      String inputModelD,
      String outputModelD,
      Duration joinWindow,
      Duration aggregationsWindow,
      int aggregationThreshold, Properties properties) {
    this.inputTopic0 = inputTopic0;
    this.inputTopic1 = inputTopic1;
    this.outputModel3 = outputModel3;
    this.outputModel1 = outputModel1;
    this.outputModel2 = outputModel2;
    this.inputModelD = inputModelD;
    this.outputModelD = outputModelD;
    this.aggregationThreshold = aggregationThreshold;
    this.properties = properties;
    this.joinWindow = joinWindow;
    this.aggregationsWindow = aggregationsWindow;
    this.mapper = defaultMapper();
  }

  public static ObjectMapper defaultMapper() {
    return com.fasterxml.jackson.databind.json.JsonMapper.builder()
        .addModule(new JavaTimeModule())
        .addModule(new Jdk8Module())
        .addModule(new ParameterNamesModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .build();
  }

  @Override
  public ObjectMapper mapper() {
    return this.mapper;
  }
}
