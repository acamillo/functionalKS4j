package fks4j.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import java.time.Duration;
import java.util.Properties;
import org.apache.kafka.streams.StreamsBuilder;

public class Main {

  public static void main(String[] args) {
    ObjectMapper mapper =
        com.fasterxml.jackson.databind.json.JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .addModule(new Jdk8Module())
            .addModule(new ParameterNamesModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build();

    Configuration cfg = new Configuration(
        "firstTopic",
        "2ndTopic",
        "diags",
        "outputTopic1",
        "outputModel2",
        Duration.ofSeconds(60),
        new Properties(),
        mapper);

    StreamsBuilder streamsBuilder = null;

//////        var sb = new AppTopology(cfg, streamsBuilder).instance();
//        var sb0 =  AppTopology.topology.runS(cfg, streamsBuilder);
////
////        var sb1 = AppTopology2.INSTANCE.topology.runS(cfg, streamsBuilder);
////        var sb2 = AppTopology3.instance.topology.runS(cfg, streamsBuilder);
////        var sb3 = AppTopology3.instance.runS(cfg, streamsBuilder);
//
//        var sb4 = KStreamSdk.combine2(
//                Topologies.detectionOne.topology,
//                Topologies.sequenceDetection.topology,
//                Topologies.detectionTypeTwo.topology).runS(cfg, streamsBuilder);
//
//        var sb5 = Topologies.all().runS(cfg, streamsBuilder);
    var sb6 = Topologies.run(cfg, streamsBuilder);
//        var app  = new TopologyRunner().buildTopology(streamsBuilder, new Properties());
  }
}
