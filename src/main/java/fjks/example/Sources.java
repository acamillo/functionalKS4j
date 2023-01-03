package fjks.example;

import fjks.example.model.EventWithTimestamp;
import fjks.kafka.streams.topology.SafeTopic;
import fjks.kafka.streams.topology.StreamBuilder;
import org.apache.kafka.common.serialization.Serdes;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.function.ToLongFunction;

public class Sources {
    public final static StreamBuilder<Configuration, SafeTopic<String, String>> topic0 =
            SafeTopic.of(c -> c.topicName0, Serdes.String(), Serdes.String());
    public final static StreamBuilder<Configuration, SafeTopic<String, String>> topic1 =
            SafeTopic.of(c -> c.topicName1, Serdes.String(), Serdes.String());
    public final static StreamBuilder<Configuration, SafeTopic<String, String>> mustBeMaterializer =
            SafeTopic.of(c -> c.topicName1, Serdes.String(), Serdes.String());


    public final static StreamBuilder<Configuration, SafeTopic<String, EventWithTimestamp>> eventTimestamped =
            SafeTopic.of(c -> c.topicName1, Serdes.String(), null, Extractor.tsExtractor);

    static class Extractor {
        final static ZoneId zone = ZoneId.of("America/Los_Angeles");
        final static ToLongFunction<EventWithTimestamp> tsExtractor =
                event -> ZonedDateTime.of(event.timeOutbound, zone).toEpochSecond() * 1000;
    }
}
