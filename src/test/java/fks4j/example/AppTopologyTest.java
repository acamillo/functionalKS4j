package fks4j.example;

import fks4j.example.model.Model1;
import fks4j.example.model.Model2;
import fks4j.example.model.Model3;
import fks4j.topology.TestKitEnv;
import fks4j.topology.config.TestConfig;
import fks4j.utillity.TestKitRunnable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.stream.Stream;
import org.apache.kafka.streams.KeyValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AppTopologyTest extends TestKitRunnable<Configuration, TestKitEnv> {

  protected AppTopologyTest() {
    super(TestConfig::build, TestKitEnv::new, AppTopology.topology, c -> c.properties);
  }

  @Test
  public void jointThreeEvents() {
    test(tkr -> {
      var dateOfBirth = LocalDateTime.of(2023, Month.FEBRUARY, 6, 18, 0);

      var m11 = new Model1("one", 40, "m1-one");
      var m12 = new Model1("two", 30, "m2-one");

      var m21 = new Model2("three", 25, "m1-two", dateOfBirth);
      var m22 = new Model2("four", 25, "m2-two", dateOfBirth.minusYears(40));

      var now = Instant.now();
      tkr.pipeOne(e -> e.model1, "k0", m11, now.plusSeconds(1));
      tkr.pipeOne(e -> e.model1, "k0", m12, now.plusSeconds(2));
      tkr.pipeOne(e -> e.model2, "k0", m21, now.plusSeconds(3));
      tkr.pipeOne(e -> e.model2, "k0", m22, now.plusSeconds(62));

      var produced = tkr.consumeAll(e -> e.outputModel3)
          .stream()
          .map(kv -> new KeyValue<>(kv.key, kv.value))
          .toList();

      var expected = Stream.of(
              new Model3("one-three", 65),
              new Model3("two-three", 55),
              new Model3("two-four", 55)
          )
          .map(m -> new KeyValue<>("k0", m))
          .toList();

      Assertions.assertEquals(expected, produced);
    });
  }

  @Test
  public void joinAllInTheWindow() {
    test(tkr -> {
      var m11 = new Model1("one", 40, "m1-one");
      var m12 = new Model1("two", 30, "m2-one");

      var m21 = new Model2("three", 25, "m1-two", LocalDateTime.now().minusYears(30));
      var m22 = new Model2("four", 25, "m2-two", LocalDateTime.now().minusYears(30));

      var m31 = new Model3("one-three", 65);
      var m32 = new Model3("two-three", 55);
      var m33 = new Model3("one-four", 65);
      var m34 = new Model3("two-four", 55);

      var now = Instant.now();
      tkr.pipeOne(e -> e.model1, "k0", m11, now.plusSeconds(1));
      tkr.pipeOne(e -> e.model1, "k0", m12, now.plusSeconds(2));
      tkr.pipeOne(e -> e.model2, "k0", m21, now.plusSeconds(3));
      tkr.pipeOne(e -> e.model2, "k0", m22, now.plusSeconds(4));

      var produced = tkr.consumeValues(e -> e.outputModel3);
      var expected = List.of(m31, m32, m33, m34);

      Assertions.assertEquals(expected, produced);
    });
  }
}
