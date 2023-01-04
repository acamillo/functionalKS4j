package fks4j.topology;

import fks4j.example.AppTopology;
import fks4j.example.Topologies;
import fks4j.example.Configuration;
import fks4j.utillity.TestKitRunnable;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Properties;
import java.util.function.Supplier;

public class AppTopologyTest extends TestKitRunnable<Configuration, TestKitEnv> {
    public final static Supplier<Configuration> config = () -> new Configuration(
            "inputTest1",
            "inputTest2",
            "outputTest",
            Duration.ofSeconds(60),
            new Properties());

//    protected AppTopologyTest() {
//        super(config, TestKitEnv::new, AppTopology.topology, c -> c.properties);
//    }
    protected AppTopologyTest() {
//        super(config, TestKitEnv::new, AppTopology3.instance.topology, c -> c.properties);
        super(config, TestKitEnv::new, Topologies.detectionOne.topology, c -> c.properties);
//        super(config, TestKitEnv::new, AppTopology.topology, c -> c.properties);
    }

    @Test
    public void safeTopic() {
        test(tkr -> {

//            tkr.assertEquals();
        });
    }
}
