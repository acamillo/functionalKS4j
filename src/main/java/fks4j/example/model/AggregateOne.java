package fks4j.example.model;

import com.fasterxml.jackson.core.type.TypeReference;
import fks4j.kafka.streams.serde.jackson.JsonSerde;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record AggregateOne(
    LocalDateTime when,
    List<T2> events
) {

  public static AggregateOne empty() {
    return new AggregateOne(LocalDateTime.now(), new ArrayList<>());
  }

  public static final JsonSerde<AggregateOne> jsonSerde = JsonSerde.gen(new TypeReference<>() {
  });
}
