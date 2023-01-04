package fks4j.example;

import fks4j.example.model.EventWithTimestamp;
import fks4j.kafka.streams.topology.SafeJoinWindow;
import fks4j.kafka.streams.topology.SafeTopic;
import fks4j.kafka.streams.topology.StreamBuilder;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.JoinWindows;

public class Sources {
    public final static StreamBuilder<Configuration, SafeTopic<String, String>> topic0 =
            SafeTopic.of(c -> c.inputTopic0, Serdes.String(), Serdes.String());
    public final static StreamBuilder<Configuration, SafeTopic<String, String>> topic1 =
            SafeTopic.of(c -> c.inputTopic1, Serdes.String(), Serdes.String());
    public final static StreamBuilder<Configuration, SafeTopic<String, String>> mustBeMaterializer =
            SafeTopic.of(c -> c.inputTopic1, Serdes.String(), Serdes.String());

    public final static StreamBuilder<Configuration, SafeTopic<String, EventWithTimestamp>> eventTimestamped =
            SafeTopic.of(c -> c.inputTopic1, Serdes.String(), null, TimeExtractors.eventWithTimestamp);

}
