package fks4j.example;

import static fks4j.kafka.streams.topology.API.compose;
import static fks4j.kafka.streams.topology.API.sinkTo;
import static fks4j.kafka.streams.topology.API.stream;

import fks4j.kafka.streams.topology.StreamBuilder;

public final class AppTopology {

  public final static StreamBuilder<Configuration, Void> topology = compose(
      Sources.model1.andThen(stream()),
      Sources.model2.andThen(stream()),
      Joins::byWindow,
      sinkTo(Sinks.model3)
  );

}
