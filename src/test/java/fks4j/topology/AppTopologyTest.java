package fks4j.topology;

import fks4j.example.AppTopology;
import fks4j.example.Configuration;
import fks4j.example.model.Model1;
import fks4j.example.model.Model2;
import fks4j.example.model.Model3;
import fks4j.topology.config.TestConfig;
import fks4j.utillity.TestKitRunnable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.kafka.streams.KeyValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class AppTopologyTest extends TestKitRunnable<Configuration, TestKitEnv> {

  protected AppTopologyTest() {
//        super(TestConfig::build, TestKitEnv::new, AppTopology3.instance.topology, c -> c.properties);
//        super(TestConfig::build, TestKitEnv::new, Topologies.detectionOne.topology, c -> c.properties);
    super(TestConfig::build, TestKitEnv::new, AppTopology.topology, c -> c.properties);
  }

  @Disabled("Needs detectionOne settings")
  @Test
  public void injectTopic1() {
    test(tkr -> {
      var m11 = new Model1("one", 40, "m1-one", LocalDateTime.now().minusYears(20));
      var m12 = new Model1("two", 30, "m2-one", LocalDateTime.now().minusYears(30));
      var m13 = new Model1("", 30, "m3-one", LocalDateTime.now().minusYears(30));

      tkr.pipeOne(e -> e.model1, "k0", m11);
      tkr.pipeOne(e -> e.model1, "k0", m13);
      tkr.pipeOne(e -> e.model1, "k0", m12);

      var produced = tkr.consumeAll(e -> e.outputModel1).stream()
          .map(kv -> new KeyValue<>(kv.key, kv.value))
          .toList();

      produced.forEach(kv -> System.out.println(kv.value));
    });
  }
  @Disabled("Needs detectionOne settings")
  @Test
  public void injectTopic2() {
    test(tkr -> {
      var m11 = new Model2("one", 40, "m1-two", LocalDateTime.now().minusYears(20));
      var m12 = new Model2("two", 30, "m2-two", LocalDateTime.now().minusYears(30));
      var m13 = new Model2("", 30, "m3-one", LocalDateTime.now().minusYears(30));

      tkr.pipeOne(e -> e.model2, "k0", m11);
      tkr.pipeOne(e -> e.model2, "k0", m13);
      tkr.pipeOne(e -> e.model2, "k0", m12);

      var produced = tkr.consumeAll(e -> e.outputModel2).stream()
          .map(kv -> new KeyValue<>(kv.key, kv.value))
          .toList();

      produced.forEach(kv -> System.out.println(kv.value));
    });
  }
  @Test
  public void jointThreeEvents() {
    test(tkr -> {
      var now = Instant.now();
      var m11 = new Model1("one", 40, "m1-one", LocalDateTime.now().minusYears(20));
      var m12 = new Model1("two", 30, "m2-one", LocalDateTime.now().minusYears(30));

      var m21 = new Model2("three", 25, "m1-two", LocalDateTime.now().minusYears(30));
      var m22 = new Model2("four", 25, "m2-two", LocalDateTime.now().minusYears(30));

      var m31 = new Model3("one-three", 65);
      var m32 = new Model3("two-three", 55);
      var m33 = new Model3("two-four", 55);

      tkr.pipeOne(e -> e.model1, "k0", m11, now.plusSeconds(1));
      tkr.pipeOne(e -> e.model1, "k0", m12, now.plusSeconds(2));
      tkr.pipeOne(e -> e.model2, "k0", m21, now.plusSeconds(3));
      tkr.pipeOne(e -> e.model2, "k0", m22, now.plusSeconds(62));

      var produced = tkr.consumeAll(e -> e.outputModel3).stream()
          .map(kv -> new KeyValue<>(kv.key, kv.value))
          .toList();

      var expected = List.of(
          new KeyValue<>("k0", m31),
          new KeyValue<>("k0", m32),
          new KeyValue<>("k0", m33)
      );

      Assertions.assertEquals(expected, produced);
    });
  }

  @Test
  public void joinAllInTheWindow() {
    test(tkr -> {
      var now = Instant.now();
      var m11 = new Model1("one", 40, "m1-one", LocalDateTime.now().minusYears(20));
      var m12 = new Model1("two", 30, "m2-one", LocalDateTime.now().minusYears(30));

      var m21 = new Model2("three", 25, "m1-two", LocalDateTime.now().minusYears(30));
      var m22 = new Model2("four", 25, "m2-two", LocalDateTime.now().minusYears(30));

      var m31 = new Model3("one-three", 65);
      var m32 = new Model3("two-three", 55);
      var m33 = new Model3("one-four", 65);
      var m34 = new Model3("two-four", 55);

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
