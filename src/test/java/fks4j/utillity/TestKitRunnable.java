package fks4j.utillity;

import fks4j.kafka.streams.serde.jackson.ConfigurableMapper;
import fks4j.kafka.streams.topology.StreamBuilder;
import java.time.Instant;
import org.apache.kafka.streams.*;
import org.junit.jupiter.api.Assertions;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Base class for Kafka Stream application's topology test.
 * The test developer needs to define a Test KIT Environment (TKE). ie. a class that contains all the input/output
 * topics the test wishes to exercise. the TKE is application dependent.
 * <p>
 * The Test Kit Runnable (TKR) embeds the concept ot Kafka Stream application's topology test benchmark.
 * In order to run the tests TKR needs the following abilities :
 * <p>
 * 1. To create a test configuration instance. This ability is provided by the Supplier `config`
 * 2. To extract the Kafka Properties from the configuration object. The properties are used for initializing the Kafka TopologyTestDriver.
 * This ability is provided by `props`
 * 3. To create an instance of `TopologyBuilder` aka the main application whose topology we want to test.
 * The `topologyBuilder` function guarantee this ability
 * 4. To create a Test KIT Environment (TKE). A TKE is application specific container with all the topics the tests wishes to exercise.
 * <p>
 * The main test class extends this one and provide the 4 required abilities when constructing the test instance.
 *
 * @param <CFG>
 */
public abstract class TestKitRunnable<CFG extends ConfigurableMapper, ENV> {
    private final Supplier<CFG>                              config;
    private final Function<CFG, Properties>                  props;
//    private final Function<CFG, TopologyBuilder>             topologyBuilder;

    private final StreamBuilder<CFG, Void> topology;
    private final Function<TestEnvironmentRuntime<CFG>, ENV> testKitEnvironment;


    /**
     * This method will execute the specified `test` passing in the Test Kit Runtime TKR.
     * TKE provides some utility methods for injecting data into the input topics, to consume data from the output topics
     * and other methods for validating the produced output against an expected result.
     * <p>
     * For each `test the method  executes the following operations:
     * 1. Create an instance of the TopologyTestDriver
     * 2. Creates an instance of the StreamsBuilder
     * 3. Execute the user-specific test
     * 4. Release the allocated resources. e.g. shutdown the Driver2
     * <p>
     * The test can exercise the input/output topics injecting/consuming data and asserting the expected behaviour
     *
     * @param test The input test
     */
    protected void test(Consumer<TestKitRuntime> test) {
        var cfg = config.get();

        var testKitTopology = Resource.make(() -> testKitTopology(cfg), TestKitTopology::close);

        testKitTopology.use(tkt -> {
            var testEnvRuntime = new TestEnvironmentRuntime<>(cfg, tkt);
            var testKitEnv = testKitEnvironment.apply(testEnvRuntime);

            var testKiRuntime = new TestKitRuntime(testKitEnv, testEnvRuntime);

            test.accept(testKiRuntime);
            return null;
        });
    }

    /**
     * Helper class to facilitate input/output topics data manipulation. An instance of it is automatically created
     * by the test framework and injected into the test code. The user can call this class method to simulate
     * input and output operations.
     */
    public final class TestKitRuntime {
        final private ENV                         env;
        final private TestEnvironmentRuntime<CFG> testEnvRuntime;

        private TestKitRuntime(ENV env, TestEnvironmentRuntime<CFG> testEnvRuntime) {
            this.env = env;
            this.testEnvRuntime = testEnvRuntime;
        }

        /**
         * To inject one record of data into the specified topic. The input needs to specified what TKE input topic to use, the key and the value.
         *
         * @param selector The topic, from the TKR, to use for the operation.
         * @param k        the Key value to inject
         * @param v        The Value part of the record
         * @param <K>      the Key data type (e.g. String)
         * @param <V>      the Value data type.
         */
        public <K, V> void pipeOne(Function<ENV, TestInputTopic<K, V>> selector, K k, V v) {
            selector.apply(env).pipeInput(k, v);
        }

        /**
         * To inject one record of data into the specified topic. The input needs to specified what TKE input topic to use, the key and the value.
         * @param selector The topic, from the TKR, to use for the operation.
         * @param k        the Key value to inject
         * @param v        The Value part of the record
         * @param when     The record timestamp
         * @param <K>      the Key data type (e.g. String)
         * @param <V>      the Value data type.
         */
        public <K, V> void pipeOne(Function<ENV, TestInputTopic<K, V>> selector, K k, V v, Instant when) {
            selector.apply(env).pipeInput(k, v, when);
        }

        /**
         * To consume (i.e. extract) all the records from a TestOutputTopic instance. The user needs to specified what TKE output topic to use.
         * The method returns a `java.util.List` of KeyValue<K,V>
         *
         * @param selector The topic, from the TKR, to use for the operation.
         * @param <K>      the Key data type (e.g. String)
         * @param <V>      the Value data type.
         */
        public <K, V> List<KeyValue<K, V>> consumeAll(Function<ENV, TestOutputTopic<K, V>> selector) {
            return selector.apply(env).readKeyValuesToList();
        }

        /**
         * To consume (i.e. extract) all the records from a TestOutputTopic instance. The user needs to specified what TKE output topic to use.
         * The method returns a `java.util.List` of <V>
         *
         * @param selector The topic, from the TKR, to use for the operation.
         * @param <K>      the Key data type (e.g. String)
         * @param <V>      the Value data type.
         */     public <K, V> List<V> consumeValues(Function<ENV, TestOutputTopic<K, V>> selector) {
            return selector.apply(env).readValuesToList();
        }

        /**
         * Helper method to verify whether two lists of KeyValue records, with String Key, contains exactly the same content.
         * In case of failure the `prettyPrinter` is called to generate a user-friendly output format of the offending data
         *
         * @param expected      The data we expect.
         * @param actual        The real data generate by the application
         * @param comparator    A comparator instance to compare the V data types
         * @param prettyPrinter a function to produce a user-friendly printing of the data set.
         * @param <V>           The Value data type.
         */
        public <V> void assertEquals(
            final List<KeyValue<String, V>> expected,
            final List<KeyValue<String, V>> actual,
            Comparator<V> comparator,
            Function<? super Object, String> prettyPrinter
        ) {
            // verify first if the two lists have the same size, if so, proceed to compare each item
            Assertions.assertEquals(expected.size(), actual.size());

            IntStream.range(0, expected.size())
                .mapToObj(i -> Map.entry(expected.get(i), actual.get(i)))
                .forEach(keyValueEntry -> {
                    try {
                        // compare first the key part, which by definition in a String, then the value part using the comparator
                        Assertions.assertEquals(keyValueEntry.getKey().key, keyValueEntry.getValue().key);
                        Assertions.assertEquals(0,
                            comparator.compare(keyValueEntry.getKey().value, keyValueEntry.getValue().value));
                    } catch (AssertionError e) {
                        var xs0 = expected.stream()
                            .map(kv -> String.format("(%s, %s)", kv.key, prettyPrinter.apply(kv.value)))
                            .collect(Collectors.joining(", "));
                        var xs1 = actual.stream()
                            .map(kv -> String.format("(%s, %s)", kv.key, prettyPrinter.apply(kv.value)))
                            .collect(Collectors.joining(", "));

                        String message =
                            "\nExpected :" + xs0 +
                                "\nActual   :" + xs1;
                        throw new AssertionError(message);
                    }
                });
        }
    }

    private TestKitTopology testKitTopology(final CFG cfg) {
        Properties p = props.apply(cfg);
        var s0 = new StreamsBuilder();
//        var streams = topologyBuilder.apply(cfg).buildTopology(s0, p);
        var streams = topology.runS(cfg, s0); // topologyBuilder.apply(cfg).buildTopology(s0, p);
        var driver = new TopologyTestDriver(streams.build(), p);
        return new TestKitTopology(driver, streams);
    }

    protected TestKitRunnable(
        Supplier<CFG> config,
        Function<TestEnvironmentRuntime<CFG>, ENV> testKitEnvironment,
//        Function<CFG, TopologyBuilder> topologyBuilder,
        final StreamBuilder<CFG, Void> topology,
        Function<CFG, Properties> props
    ) {
        this.props = props;
//        this.topologyBuilder = topologyBuilder;
        this.topology = topology;
        this.testKitEnvironment = testKitEnvironment;
        this.config = config;
    }
}