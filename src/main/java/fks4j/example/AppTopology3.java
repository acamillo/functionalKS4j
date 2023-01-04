package fks4j.example;

import fks4j.kafka.streams.topology.KStreamSdk2;
import fks4j.kafka.streams.topology.SafeTopic;
import fks4j.kafka.streams.topology.StreamBuilder;

public enum AppTopology3 implements KStreamSdk2 {

    instance;

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

    public StreamBuilder<Configuration, Void> topology = combine2(
            programOne,
            example2(Sources.topic0, Sinks.detection),
            example3()
    );

}
