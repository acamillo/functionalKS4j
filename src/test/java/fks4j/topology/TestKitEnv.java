package fks4j.topology;

import fks4j.example.Configuration;
import fks4j.example.model.Model1;
import fks4j.example.model.Model2;
import fks4j.example.model.Model3;
import fks4j.kafka.streams.topology.FSerde;
import fks4j.utillity.TestEnvironmentRuntime;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;

public final class TestKitEnv {

  public final TestInputTopic<String, Model1> model1;
  public final TestInputTopic<String, Model2> model2;
  public final TestOutputTopic<String, Model3> outputModel3;
  public final TestOutputTopic<String, Model1> outputModel1;
  public final TestOutputTopic<String, Model2> outputModel2;

  public TestKitEnv(TestEnvironmentRuntime<Configuration> tkr) {
    this(
        tkr.createStringInput2(c -> c.inputTopic0, FSerde.json(Model1.jsonSerde)),
        tkr.createStringInput2(c -> c.inputTopic1, FSerde.json(Model2.encoder, Model2.decoder)),
        tkr.createStringOutput2(c -> c.outputModel3, FSerde.json(Model3.jsonSerde)),
        tkr.createStringOutput2(c -> c.outputModel1, FSerde.json(Model1.jsonSerde)),
        tkr.createStringOutput2(c -> c.outputModel2, FSerde.json(Model2.encoder, Model2.decoder))
        );
  }

  private TestKitEnv(
      TestInputTopic<String, Model1> model1,
      TestInputTopic<String, Model2> model2,
      TestOutputTopic<String, Model3> outputModel3,
      TestOutputTopic<String, Model1> outputModel1,
      TestOutputTopic<String, Model2> outputModel2) {
    this.model1 = model1;
    this.model2 = model2;
    this.outputModel3 = outputModel3;
    this.outputModel1 = outputModel1;
    this.outputModel2 = outputModel2;
  }
}
