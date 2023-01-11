package fks4j.example;

import fks4j.example.model.Model1;
import fks4j.example.model.Model2;
import fks4j.kafka.streams.topology.FSerde;
import fks4j.kafka.streams.topology.FTopic;
import fks4j.kafka.streams.topology.StreamBuilder;

public class Sources {

//  public final static StreamBuilder<Configuration, FTopic<String, String>> topic0 =
//      FTopic.of(c -> c.inputTopic0, Serdes.String(), Serdes.String());
//  public final static StreamBuilder<Configuration, FTopic<String, String>> topic1 =
//      FTopic.of(c -> c.inputTopic1, Serdes.String(), Serdes.String());

//  public final static StreamBuilder<Configuration, FTopic<String, EventWithTimestamp>> eventTimestamped =
//      FTopic.of(c -> c.inputTopic1, Serdes.String(), null, TimeExtractors.eventWithTimestamp);

  static StreamBuilder<Configuration, FTopic<String, Model1>> model1 =
      FTopic.from(c -> c.inputTopic0, FSerde.string(), FSerde.json(Model1.jsonSerde));
  static StreamBuilder<Configuration, FTopic<String, Model2>> model2 =
      FTopic.from(c -> c.inputTopic1, FSerde.string(), FSerde.json(Model2.encoder, Model2.decoder));


}
