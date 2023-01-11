package fks4j.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import fks4j.kafka.streams.serde.jackson.JsonSerde;
import java.time.LocalDateTime;

public record Model3(
    String fused,
    int ageSum) {

  public static final JsonSerde<Model3> jsonSerde = JsonSerde.gen(new TypeReference<>() {
  });
}
