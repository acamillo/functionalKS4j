package fks4j.utillity;

import fks4j.kafka.streams.serde.jackson.ConfigurableMapper;
import fks4j.kafka.streams.topology.ConfigurableSerde;
import java.util.function.Function;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;

/**
 * Helper class for building Test Kit Environment (TKE) instances. An instance of TestEnvRuntime (TER) is automatically
 * created by the test framework and injected into the TKE building process.
 * The user can call the TER helper methods for easily creating test input and output topics
 * @param <CFG>
 */
public final class TestEnvironmentRuntime<CFG extends ConfigurableMapper> {
    private final CFG cfg;
    private final TestKitTopology testKitTopology;

    public TestEnvironmentRuntime(CFG cfg, TestKitTopology testKitTopology) {
        this.cfg = cfg;
        this.testKitTopology = testKitTopology;
    }

  /**
   * Creates a TestInputTopic with a String key, given the name extracted from the Kafka Stream main application's
   * configuration object.
   *
   * @param name   The name of the input topic.
   * @param vSerde The serde to use for value serialization.
   * @param <V>    the Value data type
   * @return a Kafka TestInputTopic instance
   */
    public <V> TestInputTopic<String, V> createStringInput(final Function<CFG, String> name, final Serde<V> vSerde) {
        return testKitTopology.driver.createInputTopic(name.apply(cfg), Serdes.String().serializer(), vSerde.serializer());
    }

  /**
   * Creates a TestInputTopic with a String key, given the name extracted from the Kafka Stream main application's
   * configuration object.
   *
   * @param name   The name of the input topic.
   * @param vSerde The configurable serde to use for value serialization.
   * @param <V>    the Value data type
   * @return a Kafka TestInputTopic instance
   */
  public <V> TestInputTopic<String, V> createStringInput(
      final Function<CFG, String> name,
      final Function<ConfigurableMapper, ? extends ConfigurableSerde<? super CFG, V>> vSerde
  ) {
    var serializer = vSerde.apply(cfg).serde(false, cfg).serializer();
    return testKitTopology.driver.createInputTopic(name.apply(cfg), Serdes.String().serializer(), serializer);
  }

  /**
   * Creates a TestOutputTopic with a String key, given the name extracted from the Kafka Stream main application's
   * configuration object.
   *
   * @param name   The name of the output topic.
   * @param vSerde The serde to use for value deserialization.
   * @param <V>    the Value data type
   * @return a Kafka TestOutputTopic instance
   */
  public <V> TestOutputTopic<String, V> createStringOutput(
      final Function<CFG, String> name,
      final Serde<V> vSerde
  ) {
    return testKitTopology.driver.createOutputTopic(name.apply(cfg), Serdes.String().deserializer(),
        vSerde.deserializer());
  }

  /**
   * Creates a TestOutputTopic with a String key, given the name extracted from the Kafka Stream main application's
   * configuration object.
   *
   * @param name   The name of the output topic.
   * @param vSerde The configurable serde to use for value serialization.
   * @param <V>    the Value data type
   * @return a Kafka TestOutputTopic instance
   */
  public <V> TestOutputTopic<String, V> createStringOutput(
      final Function<CFG, String> name,
      final Function<ConfigurableMapper, ? extends ConfigurableSerde<? super CFG, V>> vSerde
  ) {
    var deserializer = vSerde.apply(cfg).serde(false, cfg).deserializer();
    return testKitTopology.driver.createOutputTopic(name.apply(cfg), Serdes.String().deserializer(), deserializer);
  }

    public ConfigurableMapper mapper() {
        return cfg;
    }

    public CFG configuration() { return cfg; }
}