package fks4j.example.model;

import fks4j.example.Configuration;
import fks4j.kafka.streams.serde.jackson.ConfigurableMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

class Model1Test {

  final ConfigurableMapper cm = Configuration::defaultMapper;

  @Test
  void serializeAndDeserialize2() {
    var original = new Model1("John", 30, "abcd");

    var json = Model1.jsonSerde.encoder().andThen($ -> $.encode(original)).apply(cm);
    System.out.println("JSON: " + new String(json));
    var reconstructed = Model1.jsonSerde.decoder().andThen($ -> $.decode(json))
        .andThen($ -> $.getOrElseThrow(e -> new AssertionFailedError("Unable to serialize / deserialize", e)))
        .apply(cm);

    System.out.println("Decoded: " + reconstructed);
    Assertions.assertEquals(original, reconstructed);
  }
}