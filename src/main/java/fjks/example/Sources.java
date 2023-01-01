package fjks.example;

import fjks.kafka.streams.topology.SafeTopic;
import fjks.kafka.streams.topology.StreamBuilder;
import org.apache.kafka.common.serialization.Serdes;

public class Sources {
    public final static StreamBuilder<Configuration, SafeTopic<String, String>> topic0 =
            SafeTopic.build(c -> c.topicName0, Serdes.String(), Serdes.String());
    public final static StreamBuilder<Configuration, SafeTopic<String, String>> topic1 =
            SafeTopic.build(c -> c.topicName1, Serdes.String(), Serdes.String());
    public final static StreamBuilder<Configuration, SafeTopic<String, String>> mustBeMaterializer =
            SafeTopic.build(c -> c.topicName1, Serdes.String(), Serdes.String());
}
