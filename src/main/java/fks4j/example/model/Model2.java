package fks4j.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import fks4j.kafka.streams.serde.JsonDecoder;
import fks4j.kafka.streams.serde.JsonEncoder;
import fks4j.kafka.streams.serde.jackson.ConfigurableMapper;
import fks4j.kafka.streams.serde.jackson.JacksonDecoder;
import fks4j.kafka.streams.serde.jackson.JacksonEncoder;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Function;

public class Model2 {

  public final String name;
  public final int age;
  @JsonProperty("using_annotation_JsonProperty")
  public final String weirdName;
  public final LocalDateTime dateOfBirth;

  public Model2(String name, int age, String weirdName, LocalDateTime dateOfBirth) {
    this.name = name;
    this.age = age;
    this.weirdName = weirdName;
    this.dateOfBirth = dateOfBirth;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Model2 model2 = (Model2) o;
    return age == model2.age && Objects.equals(name, model2.name) && Objects.equals(weirdName,
        model2.weirdName) && Objects.equals(dateOfBirth, model2.dateOfBirth);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, age, weirdName, dateOfBirth);
  }

  @Override
  public String toString() {
    return "Model2{" +
        "name='" + name + '\'' +
        ", age=" + age +
        ", weirdName='" + weirdName + '\'' +
        ", dateOfBirth=" + dateOfBirth +
        '}';
  }

  public static final Function<ConfigurableMapper, JsonEncoder<Model2>> encoder = JacksonEncoder.gen(new TypeReference<>() {});
  public static final Function<ConfigurableMapper, JsonDecoder<Model2>> decoder = JacksonDecoder.gen(new TypeReference<>() {});
}
