package fks4j.example;

import fks4j.example.model.Model1;
import fks4j.kafka.streams.topology.KStreamSdk2;
import fks4j.kafka.streams.topology.FTopic;
import fks4j.kafka.streams.topology.StreamBuilder;

public final class AppTopology2 implements KStreamSdk2 {

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

    public final StreamBuilder<Configuration, Void> topology = combine(
            programOne,
            example2(Sources.model1, Sinks.model1),
            example3()
    );

//    public final static AppTopology2 INSTANCE = new AppTopology2();

//    private AppTopology2() {
//    }
}
