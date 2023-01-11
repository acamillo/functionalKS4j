package fks4j.example.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import fks4j.example.Configuration;
import fks4j.kafka.streams.serde.jackson.ConfigurableMapper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

class Model2Test {

  final ObjectMapper om = Configuration.defaultMapper();
  final ConfigurableMapper cm = () -> om;


  @Test
  void serializeAndDeserialize() {
    var original = new Model2("John", 30, "abcd", LocalDateTime.now().minusYears(20));

    var json = Model2.encoder.andThen($ -> $.encode(original)).apply(cm);
    System.out.println("JSON: " + new String(json));
    var reconstructed = Model2.decoder.andThen($ -> $.decode(json))
        .andThen($ -> $.getOrElseThrow(e -> new AssertionFailedError("Unable to serialize / deserialize", e)))
        .apply(cm);

    System.out.println("Decoded: " + reconstructed);
    Assertions.assertEquals(original, reconstructed);
  }
}