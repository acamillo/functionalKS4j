package fks4j.example.model;

import com.fasterxml.jackson.core.type.TypeReference;
import fks4j.kafka.streams.serde.JsonDecoder;
import fks4j.kafka.streams.serde.JsonEncoder;
import fks4j.kafka.streams.serde.jackson.ConfigurableMapper;
import fks4j.kafka.streams.serde.jackson.JacksonDecoder;
import fks4j.kafka.streams.serde.jackson.JacksonEncoder;
import java.time.LocalDateTime;
import java.util.function.Function;

public record EventWithTimestamp(LocalDateTime timeOutbound) {

    public static final Function<ConfigurableMapper,JsonEncoder<EventWithTimestamp>> encoder = JacksonEncoder.gen(new TypeReference<>() {});
    public static final Function<ConfigurableMapper,JsonDecoder<EventWithTimestamp>> decoder = JacksonDecoder.gen(new TypeReference<>() {});
}
