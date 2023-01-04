package fks4j.utillity;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;

import java.util.function.Function;

/**
 * Helper class for building Test Kit Environment (TKE) instances. An instance of TestEnvRuntime (TER) is automatically
 * created by the test framework and injected into the TKE building process.
 * The user can call the TER helper methods for easily creating test input and output topics
 * @param <CFG>
 */
public final class TestEnvironmentRuntime<CFG> {
    private final CFG cfg;
    private final TestKitTopology testKitTopology;

    public TestEnvironmentRuntime(CFG cfg, TestKitTopology testKitTopology) {
        this.cfg = cfg;
        this.testKitTopology = testKitTopology;
    }

    /**
     * Creates a TestInputTopic with a String key, given the name extracted from the Kafka Stream main application's configuration object.
     *
     * @param name The name of the input topic.
     * @param vSerde The serde to use for value serialization.
     * @return a Kafka TestInputTopic instance
     * @param <V> the Value data type
     */
    public <V> TestInputTopic<String, V> createStringInput(final Function<CFG, String> name, final Serde<V> vSerde) {
        return testKitTopology.driver.createInputTopic(name.apply(cfg), Serdes.String().serializer(), vSerde.serializer());
    }

    /**
     * Creates a TestOutputTopic with a String key, given the name extracted from the Kafka Stream main application's configuration object.
     *
     * @param name The name of the output topic.
     * @param vSerde The serde to use for value deserialization.
     * @return a Kafka TestOutputTopic instance
     * @param <V> the Value data type
     */
    public <V> TestOutputTopic<String, V> createStringOutput(final Function<CFG, String> name, final Serde<V> vSerde) {
        return testKitTopology.driver.createOutputTopic(name.apply(cfg), Serdes.String().deserializer(), vSerde.deserializer());
    }
}