package fks4j.example;

import fks4j.kafka.streams.topology.KStreamSdk2;
import fks4j.kafka.streams.topology.SafeTopic;
import fks4j.kafka.streams.topology.StreamBuilder;

public final class AppTopology2 implements KStreamSdk2 {

    private final StreamBuilder<Configuration, Void> programOne = compose(
            Sources.topic0.andThen(stream()).map(ks -> ks.filter((k, v) -> !v.isEmpty())),
            sinkTo(Sinks.detection)
    );

    private StreamBuilder<Configuration, Void> example2(
            StreamBuilder<Configuration, SafeTopic<String, String>> in,
            StreamBuilder<Configuration, SafeTopic<String, String>> out
    ) {
        return compose(
                in.andThen(stream()),
                ks -> {
                    var k1 = ks.filter((k, v) -> !v.isEmpty());
                    return sinkTo(out).apply(k1);
                }
        );
    }

    private StreamBuilder<Configuration, Void> example3() {
        return Sources.topic0.andThen(stream())
                .map(ks -> ks.filter((k, v) -> !v.isEmpty()))
                .flatMap(ks -> Sinks.detection.andThen(sink(ks)));
    }

    public final StreamBuilder<Configuration, Void> topology = combine(
            programOne,
            example2(Sources.topic0, Sinks.detection),
            example3()
    );

//    public final static AppTopology2 INSTANCE = new AppTopology2();

//    private AppTopology2() {
//    }
}
