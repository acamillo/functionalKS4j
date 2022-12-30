package fjks.kafka.streams.topology;

import fjks.example.Configuration;

public interface KafkaStreamsApp {
    StreamBuilder<Configuration, Void> instance() ;
}
