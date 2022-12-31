package fjks.example;

import fjks.kafka.streams.topology.SafeTopic;
import fjks.kafka.streams.topology.StreamBuilder;

import static fjks.kafka.streams.topology.KStreamSdk.*;

//public class AppTopology implements KafkaStreamsApp, Syntax {
public final class AppTopology {

    private final static StreamBuilder<Configuration, Void> auditDTopology = compose(
            Sources.auditD.andThen(stream()).map(ks -> ks.filter((k, v) -> !v.isEmpty())),
            sinkTo(Sinks.detection)
//            ks -> Sinks.detection.andThen(sinkTo(ks))
    );

    private  static  StreamBuilder<Configuration, Void> example2(
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
        return Sources.auditD.andThen(stream())
                .map(ks -> ks.filter((k, v) -> !v.isEmpty()))
                .flatMap(ks -> Sinks.detection.andThen(sink(ks)));
    }

    private static StreamBuilder<Configuration, Void> example4() {
        return compose(
                Sources.auditD.andThen(stream())
                        .map(ks -> ks.filter((k, v) -> !v.isEmpty())),
                sinkTo(Sinks.detection)
//            ks -> Sinks.detection.andThen(sinkTo(ks))
        );
    }

    public final static StreamBuilder<Configuration, Void> topology = combine2(
                auditDTopology,
                example2(Sources.auditD, Sinks.detection),
                example3()
        );

//    public  final StreamBuilder<Configuration, Void> instance() {
//        return combine(
//                auditDTopology,
//                example2(Sources.auditD, Sinks.detection),
//                example3()
//        );
//    }

}
