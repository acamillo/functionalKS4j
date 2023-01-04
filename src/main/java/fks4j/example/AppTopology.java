package fks4j.example;

import fks4j.kafka.streams.topology.SafeTopic;
import fks4j.kafka.streams.topology.StreamBuilder;
import org.apache.kafka.streams.kstream.KStream;

import static fks4j.kafka.streams.topology.KStreamSdk.*;

//public class AppTopology implements KafkaStreamsApp, Syntax {
public final class AppTopology {

    private final static StreamBuilder<Configuration, Void> programOne = compose(
            Sources.topic0.andThen(stream()).map(ks -> ks.filter((k, v) -> !v.isEmpty())),
            sinkTo(Sinks.detection)
//            ks -> Sinks.detection.andThen(sinkTo(ks))
    );

    private static StreamBuilder<Configuration, Void> example2(
            StreamBuilder<Configuration, SafeTopic<String, String>> in,
            StreamBuilder<Configuration, SafeTopic<String, String>> out
    ) {
        return compose(
                in.andThen(stream()),
                ks -> {
                    var k1 = ks.filter((k, v) -> !v.isEmpty());
//                    return out.andThen(sink(k1));
                    return sinkTo(out).apply(k1);
                }
        );
    }


    private static StreamBuilder<Configuration, Void> example3() {
        return Sources.topic0.andThen(stream())
                .map(ks -> ks.filter((k, v) -> !v.isEmpty()))
                .flatMap(ks -> Sinks.detection.andThen(sink(ks)));
    }

    private static StreamBuilder<Configuration, Void> example4() {
        return compose(
                Sources.topic0.andThen(stream())
                        .map(ks -> ks.filter((k, v) -> !v.isEmpty())),
                sinkTo(Sinks.detection)
//            ks -> Sinks.detection.andThen(sinkTo(ks))
        );
    }

    private StreamBuilder<Configuration, KStream<String, String>>
    doJoin(KStream<String, String> ks0, KStream<String, String> ks1) {

        return Windows.topic0_1Window.map(joinWindow ->
                ks0.join(ks1, (v0, v1) -> v0 + v1, joinWindow));
    }

    private static StreamBuilder<Configuration, Void> joinTwoSafeTopics(
            StreamBuilder<Configuration, SafeTopic<String, String>> t0,
            StreamBuilder<Configuration, SafeTopic<String, String>> t1,
            StreamBuilder<Configuration, SafeTopic<String, String>> out
    ) {
        return t0.andThen(stream()).flatMap(ks0 ->
                        t1.andThen(stream()).map(ks1 ->
                                ks0.join(ks1, (v0, v1) -> v0 + v1, null))
                ).flatMap(sinkTo(out));
    }

    public final static StreamBuilder<Configuration, Void> topology = combine2(
            programOne,
            example2(Sources.topic0, Sinks.detection),
            example3()
    );

}
