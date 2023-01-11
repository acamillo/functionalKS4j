package fks4j.example;

import fks4j.example.model.Model1;
import fks4j.kafka.streams.topology.KStreamSdk2;
import fks4j.kafka.streams.topology.FTopic;
import fks4j.kafka.streams.topology.StreamBuilder;

public enum AppTopology3 implements KStreamSdk2 {

    instance;

    private final StreamBuilder<Configuration, Void> programOne = compose(
            Sources.model1.andThen(stream()).map(ks -> ks.filter((k, v) -> !v.name().isEmpty())),
            sinkTo(Sinks.model1)
    );

    private StreamBuilder<Configuration, Void> example2(
            StreamBuilder<Configuration, FTopic<String, Model1>> in,
            StreamBuilder<Configuration, FTopic<String, Model1>> out
    ) {
        return compose(
                in.andThen(stream()),
                ks -> {
                    var k1 = ks.filter((k, v) -> !v.name().isEmpty());
                    return sinkTo(out).apply(k1);
                }
        );
    }

    private StreamBuilder<Configuration, Void> example3() {
        return Sources.model1.andThen(stream())
                .map(ks -> ks.filter((k, v) -> !v.name().isEmpty()))
                .flatMap(ks -> Sinks.model1.andThen(sink(ks)));
    }

    public StreamBuilder<Configuration, Void> topology = combine2(
            programOne,
            example2(Sources.model1, Sinks.model1),
            example3()
    );

}
