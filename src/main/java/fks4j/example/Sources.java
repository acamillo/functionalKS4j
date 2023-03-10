package fks4j.example;

import static fks4j.example.TimeExtractors.toMillis;

import fks4j.example.model.Model1;
import fks4j.example.model.Model2;
import fks4j.example.model.ModelD;
import fks4j.kafka.streams.topology.FSerde;
import fks4j.kafka.streams.topology.FTopic;
import fks4j.kafka.streams.topology.StreamBuilder;

public class Sources {
  static StreamBuilder<Configuration, FTopic<String, Model1>> model1 =
      FTopic.from(c -> c.inputTopic0, FSerde.string(), FSerde.json(Model1.jsonSerde));
  static StreamBuilder<Configuration, FTopic<String, Model2>> model2 =
      FTopic.from(c -> c.inputTopic1, FSerde.string(), FSerde.json(Model2.encoder, Model2.decoder));

  static StreamBuilder<Configuration, FTopic<String, ModelD>> modelD =
      FTopic.from(c-> c.inputModelD, FSerde.string(), FSerde.json(ModelD.jsonSerde), e -> toMillis(e.timestamp()));
}
