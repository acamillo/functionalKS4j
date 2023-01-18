package fks4j.example;

import static fks4j.kafka.streams.topology.API.compose;
import static fks4j.kafka.streams.topology.API.sinkTo;
import static fks4j.kafka.streams.topology.API.stream;

import fks4j.example.model.Model1;
import fks4j.example.model.Model2;
import fks4j.example.model.Model3;
import fks4j.kafka.streams.topology.FTopic;
import fks4j.kafka.streams.topology.StreamBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.ValueJoiner;

public final class AppTopology {

  public final static StreamBuilder<Configuration, Void> programOne = compose(
      Sources.model1.andThen(stream()).map(ks -> ks.filter((k, v) -> !v.name().isEmpty())),
      sinkTo(Sinks.model1)
  );

  public final static StreamBuilder<Configuration, Void> programTwo = compose(
      Sources.model2.andThen(stream()).map(ks -> ks.filter((k, v) -> !v.name.isEmpty())),
      sinkTo(Sinks.model2)
  );

  private static StreamBuilder<Configuration, KStream<String, Model3>> doJoin(
      KStream<String, Model1> ks0,
      KStream<String, Model2> ks1
  ) {

    ValueJoiner<Model1, Model2, Model3> joiner = (m1, m2) -> new Model3(m1.name() + "-" + m2.name, m1.age() + m2.age);
    return Joiners.m1m2.map(fJoiner -> fJoiner.join(ks0, ks1, joiner));
  }


  private static StreamBuilder<Configuration, Void> joinTwoFTopics(
      StreamBuilder<Configuration, FTopic<String, Model1>> m1,
      StreamBuilder<Configuration, FTopic<String, Model2>> m2
  ) {
    return
        m1.andThen(stream())
            .flatMap(ksM1 -> m2.andThen(stream())
                .flatMap(ksM2 -> doJoin(ksM1, ksM2)))
            .flatMap(sinkTo(Sinks.model3));
  }

  public final static StreamBuilder<Configuration, Void> topology =
      joinTwoFTopics(Sources.model1, Sources.model2);

//  public final static StreamBuilder<Configuration, Void> topology = combine2(
//      programOne,
//      joinTwoSafeTopics2(Sources.model1, Sources.model2)
////      example2(Sources.model1, Sinks.model1),
////      example3()
//  );

}
