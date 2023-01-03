package fjks.example;

import fjks.kafka.streams.topology.SafeTopic;
import fjks.kafka.streams.topology.StreamBuilder;
import org.apache.kafka.common.serialization.Serdes;

public class Sinks {
    public final static StreamBuilder<Configuration, SafeTopic<String, String>> detection =
            SafeTopic.of(c -> c.detectionTopicName, Serdes.String(), Serdes.String());
}
