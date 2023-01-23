package fks4j.example;

import static fks4j.kafka.streams.topology.API.compose;
import static fks4j.kafka.streams.topology.API.sinkTo;
import static fks4j.kafka.streams.topology.API.stream;

import fks4j.example.model.AggregateOne;
import fks4j.example.model.ModelD;
import fks4j.kafka.streams.topology.StreamBuilder;
import fks4j.topology.TestKitAggregations;
import fks4j.topology.config.TestConfig;
import fks4j.utillity.TestKitRunnable;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AggregateTopologyTest extends TestKitRunnable<Configuration, TestKitAggregations> {

  protected AggregateTopologyTest() {
    super(TestConfig::build, TestKitAggregations::new, topology, c -> c.properties);
  }

  public final static StreamBuilder<Configuration, Void> topology = compose(
      Sources.modelD.andThen(stream()),
      Aggregates::bySliding,
      sinkTo(Sinks.aggregateD)
  );


  @Test
  public void aggregateModelDWithTimeExtractor() {
    test(tkr -> {
      var numberOfEvents = Math.max(14, tkr.configuration().aggregationThreshold);
      var ldt = LocalDateTime.of(2023, Month.JANUARY, 6, 18, 0);

      var ev0 = IntStream
          .rangeClosed(1, numberOfEvents)
          .mapToObj(i -> new ModelD(String.valueOf(i), ldt.plusSeconds(i)))
          .toList();

      var ev1 = List.of(
          new ModelD("61", ldt.plusSeconds(61)),
          new ModelD("bogus", ldt.plusSeconds(99))
      );

      tkr.pipeMany(e -> e.modelD, "k0", ev0);
      tkr.pipeMany(e -> e.modelD, "k0", ev1);

      var jsonEncoder = tkr.encoder(AggregateOne.jsonSerde);
      var produced = tkr.consumeValues(e -> e.outputAggregate);

      System.out.println("Aggregation window duration (secs): " + tkr.configuration().aggregationsWindow.toSeconds());
      System.out.println("Aggregation threshold (events): " + tkr.configuration().aggregationThreshold);
      produced
          .stream()
          .map(v -> new String(jsonEncoder.encode(v)))
          .forEach(System.out::println);

      Assertions.assertEquals(produced.size(), 1 + numberOfEvents - tkr.configuration().aggregationThreshold);
      produced.forEach(v -> Assertions.assertTrue(v.events().size() >= tkr.configuration().aggregationThreshold));
    });
  }
}