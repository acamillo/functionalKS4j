package fks4j.utillity;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.TopologyTestDriver;

public final class TestKitTopology {
    public final TopologyTestDriver driver;
    public final StreamsBuilder     streamsBuilder;

    public TestKitTopology(final TopologyTestDriver driver, final StreamsBuilder streamsBuilder) {
        this.driver = driver;
        this.streamsBuilder = streamsBuilder;
    }

    public void close() {
        driver.close();
    }
}