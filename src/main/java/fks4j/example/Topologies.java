package fks4j.example;

import fks4j.kafka.streams.topology.StreamBuilder;
import java.util.Arrays;
import org.apache.kafka.streams.StreamsBuilder;

public enum Topologies  {

    detectionOne(AppTopology.topology),
    detectionTypeTwo(new AppTopology2().topology),
    sequenceDetection(AppTopology3.instance.topology);

    public final StreamBuilder<Configuration, Void> topology;
    Topologies(StreamBuilder<Configuration, Void> topology) {
        this.topology = topology;
    }

    static StreamBuilder<Configuration, Void> all() {
        return Arrays.stream(Topologies.values()).map($->$.topology)
                .reduce((acc, elem) -> acc.flatMap($ -> elem))
                .orElseThrow(() -> new IllegalArgumentException("all requires a minimum of two elements"));
    }
    static StreamsBuilder run(Configuration cfg, StreamsBuilder sb) {
        return all().runS(cfg, sb);
    }


}
