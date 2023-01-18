package fks4j.example;

import fks4j.example.model.Model1;
import fks4j.example.model.Model2;
import fks4j.kafka.streams.topology.FJoiner;
import fks4j.kafka.streams.topology.FSerde;
import fks4j.kafka.streams.topology.StreamBuilder;

public class Joiners {
  static StreamBuilder<Configuration, FJoiner<String, Model1, Model2>> m1m2 =
      FJoiner.of(c -> c.joinWindow, FSerde.string(), FSerde.json(Model1.jsonSerde),
          FSerde.json(Model2.encoder, Model2.decoder));
}
