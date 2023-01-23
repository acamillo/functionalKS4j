package fks4j.topology;

import fks4j.example.Configuration;
import fks4j.example.model.AggregateOne;
import fks4j.example.model.ModelD;
import fks4j.kafka.streams.topology.FSerde;
import fks4j.utillity.TestEnvironmentRuntime;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;

public final class TestKitAggregations {


  public final TestInputTopic<String, ModelD> modelD;
  public final TestOutputTopic<String, AggregateOne> outputAggregate;


  public TestKitAggregations(TestEnvironmentRuntime<Configuration> tkr) {
    this(
        tkr.createStringInput(c -> c.inputModelD, FSerde.json(ModelD.jsonSerde)),
        tkr.createStringOutput(c -> c.outputModelD, FSerde.json(AggregateOne.jsonSerde))
    );
  }

  private TestKitAggregations(
      TestInputTopic<String, ModelD> modelD,
      TestOutputTopic<String, AggregateOne> outputAggregate
  ) {
    this.modelD = modelD;
    this.outputAggregate = outputAggregate;
  }
}
