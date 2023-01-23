package fks4j.example;

import fks4j.example.model.Model1;
import fks4j.example.model.Model2;
import fks4j.example.model.Model3;
import fks4j.kafka.streams.topology.FJoiner;
import fks4j.kafka.streams.topology.FSerde;
import fks4j.kafka.streams.topology.StreamBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.ValueJoiner;

public class Joins {

  static StreamBuilder<Configuration, FJoiner<String, Model1, Model2>> m1m2 =
      FJoiner.of(
          c -> c.joinWindow,
          FSerde.string(),
          FSerde.json(Model1.jsonSerde),
          FSerde.json(Model2.encoder, Model2.decoder));

  public static StreamBuilder<Configuration, KStream<String, Model3>> byWindow(
      KStream<String, Model1> ks0,
      KStream<String, Model2> ks1
  ) {

    return m1m2.map(fJoiner -> fJoiner.join(ks0, ks1, Joiners.m1m2));
  }

  private static class Joiners {
    static final ValueJoiner<Model1, Model2, Model3> m1m2 = (m1, m2) ->
        new Model3(m1.name() + "-" + m2.name, m1.age() + m2.age);
  }
}
