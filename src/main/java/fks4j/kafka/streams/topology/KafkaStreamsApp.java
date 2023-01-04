package fks4j.kafka.streams.topology;

import fks4j.example.Configuration;

public interface KafkaStreamsApp {
    StreamBuilder<Configuration, Void> instance() ;
}
