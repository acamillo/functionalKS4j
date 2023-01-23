package fks4j.example.model;

import com.fasterxml.jackson.core.type.TypeReference;
import fks4j.kafka.streams.serde.jackson.JsonSerde;
import java.time.LocalDateTime;

public record ModelD(
    String type,
    LocalDateTime timestamp
) {

  public static final JsonSerde<ModelD> jsonSerde = JsonSerde.gen(new TypeReference<>() {
  });
}
