package fks4j.topology;

import fks4j.example.Configuration;
import fks4j.example.model.Model1;
import fks4j.example.model.Model2;
import fks4j.utillity.TestEnvironmentRuntime;
import org.apache.kafka.streams.TestInputTopic;

public final class TestKitEnv {

    public final TestInputTopic<String, Model1> model1;
    public final TestInputTopic<String, Model2> model2;

    public TestKitEnv(TestEnvironmentRuntime<Configuration> tkr) {
        this(
                tkr.createStringInput(c -> c.inputTopic0, null),
                tkr.createStringInput(c -> c.inputTopic1, null)
        );
    }

    private TestKitEnv(
            TestInputTopic<String, Model1> model1,
            TestInputTopic<String, Model2> model2
    ) {
        this.model1 = model1;
        this.model2 = model2;
    }
}
