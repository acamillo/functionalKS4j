package fks4j.example;

import static fks4j.kafka.streams.topology.KStreamSdk.combine2;
import static fks4j.kafka.streams.topology.KStreamSdk.compose;
import static fks4j.kafka.streams.topology.KStreamSdk.sinkTo;
import static fks4j.kafka.streams.topology.KStreamSdk.stream;

import fks4j.example.model.Model1;
import fks4j.example.model.Model2;
import fks4j.example.model.Model3;
import fks4j.kafka.streams.topology.FTopic;
import fks4j.kafka.streams.topology.StreamBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.ValueJoiner;

//public class AppTopology implements KafkaStreamsApp, Syntax {
public final class AppTopology {

  public final static StreamBuilder<Configuration, Void> programOne = compose(
      Sources.model1.andThen(stream()).map(ks -> ks.filter((k, v) -> !v.name().isEmpty())),
      sinkTo(Sinks.model1)
  );

  public final static StreamBuilder<Configuration, Void> programTwo = compose(
      Sources.model2.andThen(stream()).map(ks -> ks.filter((k, v) -> !v.name.isEmpty())),
      sinkTo(Sinks.model2)
  );

//  private static StreamBuilder<Configuration, Void> example2(
//      StreamBuilder<Configuration, FTopic<String, Model1>> in,
//      StreamBuilder<Configuration, FTopic<String, Model1>> out
//  ) {
//    return compose(
//        in.andThen(stream()),
//        ks -> {
//          var k1 = ks.filter((k, v) -> !v.name().isEmpty());
////                    return out.andThen(sink(k1));
//          return sinkTo(out).apply(k1);
//        }
//    );
//  }


//  private static StreamBuilder<Configuration, Void> example3() {
//    return Sources.model1.andThen(stream())
//        .map(ks -> ks.filter((k, v) -> !v.name().isEmpty()))
//        .flatMap(ks -> Sinks.model1.andThen(sink(ks)));
//  }
//
//  private static StreamBuilder<Configuration, Void> example4() {
//    return compose(
//        Sources.model1.andThen(stream())
//            .map(ks -> ks.filter((k, v) -> !v.name().isEmpty())),
//        sinkTo(Sinks.model1)
////            ks -> Sinks.model1.andThen(sinkTo(ks))
//    );
//  }

  private static StreamBuilder<Configuration, KStream<String, Model3>> doJoin(
      KStream<String, Model1> ks0,
      KStream<String, Model2> ks1
  ) {

    ValueJoiner<Model1, Model2, Model3> joiner = (m1, m2) -> new Model3(m1.name() + "-" + m2.name, m1.age() + m2.age);
    return Windows.topic0_1Window.map(joinWindow -> ks0.join(ks1, joiner, joinWindow));
  }


  private static StreamBuilder<Configuration, Void> joinTwoSafeTopics2(
      StreamBuilder<Configuration, FTopic<String, Model1>> m1,
      StreamBuilder<Configuration, FTopic<String, Model2>> m2
  ) {
    return
        m1.andThen(stream())
            .flatMap(ksM1 -> m2.andThen(stream())
                .flatMap(ksM2 -> doJoin(ksM1, ksM2)))
            .flatMap(sinkTo(Sinks.model3));
  }

//  private static StreamBuilder<Configuration, Void> joinTwoSafeTopics(
//      StreamBuilder<Configuration, FTopic<String, Model1>> t0,
//      StreamBuilder<Configuration, FTopic<String, Model2>> t1,
//      StreamBuilder<Configuration, FTopic<String, Model1>> out
//  ) {
//    ValueJoiner<Model1, Model2, Model1> joiner = (m1, m2) -> new Model1(m1.name(), m1.age() + m2.age,
//        m1.name() + m2.name, m1.dateOfBirth());
//
//    return t0.andThen(stream()).flatMap(ks0 ->
//        t1.andThen(stream()).map(ks1 ->
//            ks0.join(ks1, joiner, null))
//    ).flatMap(sinkTo(out));
//  }

  public final static StreamBuilder<Configuration, Void> topology =
      joinTwoSafeTopics2(Sources.model1, Sources.model2);


//  public final static StreamBuilder<Configuration, Void> topology = combine2(
//      programOne,
//      joinTwoSafeTopics2(Sources.model1, Sources.model2)
////      example2(Sources.model1, Sinks.model1),
////      example3()
//  );

}
