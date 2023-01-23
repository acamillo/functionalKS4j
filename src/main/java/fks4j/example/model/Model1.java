package fks4j.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import fks4j.kafka.streams.serde.jackson.JsonSerde;

public record Model1(
    String name,
    int age,
    @JsonProperty("using_annotation_JsonProperty") String weirdName
//    LocalDateTime dateOfBirth
) {

  public static final JsonSerde<Model1> jsonSerde = JsonSerde.gen(new TypeReference<>() {
  });
}
