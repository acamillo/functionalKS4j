package fks4j.example;

import fks4j.kafka.streams.topology.SafeTopic;
import fks4j.kafka.streams.topology.StreamBuilder;
import org.apache.kafka.common.serialization.Serdes;

public class Sinks {
    public final static StreamBuilder<Configuration, SafeTopic<String, String>> detection =
            SafeTopic.of(c -> c.outputTopic0, Serdes.String(), Serdes.String());
}
