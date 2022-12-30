package fjks.example;

import fjks.kafka.streams.topology.SafeTopic;
import fjks.kafka.streams.topology.StreamBuilder;
import org.apache.kafka.common.serialization.Serdes;

public class Sources {
    public final static StreamBuilder<Configuration, SafeTopic<String, String>> auditD =
            SafeTopic.build(c -> c.auditdTopicName, Serdes.String(), Serdes.String());
}
