package fks4j.example;

import fks4j.kafka.streams.topology.KStreamSdk;
import org.apache.kafka.streams.StreamsBuilder;

import java.time.Duration;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Configuration cfg = new Configuration(
                "firstTopic",
                "2ndTopic",
                "diags",
                Duration.ofSeconds(60),
                new Properties());
        StreamsBuilder streamsBuilder = null;

////        var sb = new AppTopology(cfg, streamsBuilder).instance();
        var sb0 =  AppTopology.topology.runS(cfg, streamsBuilder);
//
//        var sb1 = AppTopology2.INSTANCE.topology.runS(cfg, streamsBuilder);
//        var sb2 = AppTopology3.instance.topology.runS(cfg, streamsBuilder);
//        var sb3 = AppTopology3.instance.runS(cfg, streamsBuilder);

        var sb4 = KStreamSdk.combine2(
                Topologies.detectionOne.topology,
                Topologies.sequenceDetection.topology,
                Topologies.detectionTypeTwo.topology).runS(cfg, streamsBuilder);

        var sb5 = Topologies.all().runS(cfg, streamsBuilder);
        var sb6 = Topologies.run(cfg, streamsBuilder);
//        var app  = new TopologyRunner().buildTopology(streamsBuilder, new Properties());
    }
}
