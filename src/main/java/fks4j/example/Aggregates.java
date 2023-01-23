package fks4j.example;

import fks4j.example.model.AggregateOne;
import fks4j.example.model.ModelD;
import fks4j.example.model.T2;
import fks4j.kafka.streams.topology.FAggregator;
import fks4j.kafka.streams.topology.FSerde;
import fks4j.kafka.streams.topology.SessionWindowAggregator;
import fks4j.kafka.streams.topology.SlidingWindowAggregator;
import fks4j.kafka.streams.topology.StreamBuilder;
import fks4j.kafka.streams.topology.TimeWindowAggregator;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Aggregator;
import org.apache.kafka.streams.kstream.Initializer;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Merger;
import org.apache.kafka.streams.kstream.Suppressed;
import org.apache.kafka.streams.kstream.Suppressed.BufferConfig;

public class Aggregates {

  static StreamBuilder<Configuration, SlidingWindowAggregator<String, ModelD, AggregateOne>> sliding =
      FAggregator.sliding(Windows.sliding, FSerde.string(), FSerde.json(AggregateOne.jsonSerde));
  static StreamBuilder<Configuration, SessionWindowAggregator<String, ModelD, AggregateOne>> session =
      FAggregator.session(Windows.session, FSerde.string(), FSerde.json(AggregateOne.jsonSerde));

  static StreamBuilder<Configuration, TimeWindowAggregator<String, ModelD, AggregateOne>> tumbling =
      FAggregator.tumbling(Windows.tumbling, FSerde.string(), FSerde.json(AggregateOne.jsonSerde));


  public static StreamBuilder<Configuration, KStream<String, AggregateOne>> bySliding(KStream<String, ModelD> modelD) {
    return sliding.flatMap(aggregator ->
            StreamBuilder.<Configuration>environment().map(cfg ->
                aggregator
                    .aggregateByKey(
                        modelD,
                        Aggregators.initialize,
                        Aggregators.aggregate
                    )
                    .suppress(Suppressed.untilWindowCloses(BufferConfig.unbounded()))
                    .toStream()
                    .map((wk, wv) -> KeyValue.pair(wk.key(), wv))
                    .filter((k, v) -> v.events().size() >= cfg.aggregationThreshold)
            )
//            .peek((k, v) -> System.out.println("key = " + k + ", value: " + v))
    );
  }

  public static StreamBuilder<Configuration, KStream<String, AggregateOne>> bySession(KStream<String, ModelD> modelD) {
    return session.map(aggregator ->
            aggregator
                .aggregateByKey(
                    modelD,
                    Aggregators.initialize,
                    Aggregators.aggregate,
                    Aggregators.merge
                )
                .suppress(Suppressed.untilWindowCloses(BufferConfig.unbounded()))
                .toStream()
                .map((wk, wv) -> KeyValue.pair(wk.key(), wv))
//            .peek((k, v) -> System.out.println("key = " + k + ", value: " + v))
    );
  }

  public static StreamBuilder<Configuration, KStream<String, AggregateOne>> byTumbling(KStream<String, ModelD> modelD) {
    return tumbling.map(aggregator ->
            aggregator
                .aggregateByKey(
                    modelD,
                    Aggregators.initialize,
                    Aggregators.aggregate
                )
                .suppress(Suppressed.untilWindowCloses(BufferConfig.unbounded()))
                .toStream()
                .map((wk, wv) -> KeyValue.pair(wk.key(), wv))
//            .peek((k, v) -> System.out.println("key = " + k + ", value: " + v))
    );
  }

  private static class Aggregators {

    final static Initializer<AggregateOne> initialize = AggregateOne::empty;
    final static Aggregator<String, ModelD, AggregateOne> aggregate = (k, event, acc) -> {
      var events = acc.events();
      events.add(new T2(event.type(), event.timestamp()));
      return new AggregateOne(acc.when(), events);
    };

    final static Merger<String, AggregateOne> merge = (k, v1, v2) -> {
      var events = v1.events();
      events.addAll(v2.events());
      return new AggregateOne(v1.when(), events);
    };
  }
}

