package fks4j.example;

import fks4j.example.model.AggregateOne;
import fks4j.example.model.Model1;
import fks4j.example.model.Model2;
import fks4j.example.model.Model3;
import fks4j.kafka.streams.topology.FSerde;
import fks4j.kafka.streams.topology.FTopic;
import fks4j.kafka.streams.topology.StreamBuilder;

public class Sinks {

  public final static StreamBuilder<Configuration, FTopic<String, Model1>> model1 =
      FTopic.from(c -> c.outputModel1, FSerde.string(), FSerde.json(Model1.jsonSerde));

  public final static StreamBuilder<Configuration, FTopic<String, Model3>> model3 =
      FTopic.from(c -> c.outputModel3, FSerde.string(), FSerde.json(Model3.jsonSerde));
  public final static StreamBuilder<Configuration, FTopic<String, Model2>> model2 =
      FTopic.from(c -> c.outputModel2, FSerde.string(), FSerde.json(Model2.encoder, Model2.decoder));

  public final static StreamBuilder<Configuration, FTopic<String, AggregateOne>> aggregateD =
      FTopic.from(c -> c.outputModelD, FSerde.string(), FSerde.json(AggregateOne.jsonSerde));
}
