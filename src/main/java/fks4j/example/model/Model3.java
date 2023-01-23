package fks4j.example.model;

import com.fasterxml.jackson.core.type.TypeReference;
import fks4j.kafka.streams.serde.jackson.JsonSerde;

public record Model3(
    String fused,
    int ageSum) {

  public static final JsonSerde<Model3> jsonSerde = JsonSerde.gen(new TypeReference<>() {
  });
}
