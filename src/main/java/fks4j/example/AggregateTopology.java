package fks4j.example;

import static fks4j.kafka.streams.topology.API.compose;
import static fks4j.kafka.streams.topology.API.sinkTo;
import static fks4j.kafka.streams.topology.API.stream;

import fks4j.kafka.streams.topology.StreamBuilder;

public final class AggregateTopology {

  public final static StreamBuilder<Configuration, Void> topology = compose(
      Sources.modelD.andThen(stream()),
      Aggregates::bySession,
      sinkTo(Sinks.aggregateD)
  );

}
