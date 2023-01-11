package fks4j.kafka.streams.topology;

import fks4j.kafka.streams.serde.JsonDecoder;
import fks4j.kafka.streams.serde.JsonEncoder;
import fks4j.kafka.streams.serde.jackson.ConfigurableMapper;
import fks4j.kafka.streams.serde.jackson.JsonSerde;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;


public final class FSerde {

  public record StaticSerde<T>(Serde<T> stats) implements ConfigurableSerde<Object, T> {

    @Override
    public Serde<T> serde(Boolean isKey, Object o) {
      return stats;
    }

//    @Override
//    public BiFunction<Boolean, Object, Serde<T>> serde() {
//      return ($, $$) -> stats;
//    }
  }


  private static <T> StaticSerde<T> serde(
      BiFunction<String, T, byte[]> ser,
      BiFunction<String, byte[], T> deser
  ) {
    return new StaticSerde<>(Serdes.serdeFrom(
        new Serializer<>() {
          @Override
          public byte[] serialize(String topic, T data) {
            return ser.apply(topic, data);
          }
        },
        new Deserializer<>() {
          @Override
          public T deserialize(String topic, byte[] data) {
            return deser.apply(topic, data);
          }
        }
    ));
  }

  /**
   * Given the instance of a JsonEncoder<T> and a  JsonDecoder<T> is creates an instance of a Serde<T></T>
   * @param encoder a function which given a ConfigurableMapper instance generates an instance of a  JsonEncoder<T>
   * @param decoder a function which given a ConfigurableMapper instance generates an instance of a  JsonDecoder<T></T>
   * @return a function which given a ConfigurableMapper instance generates an instance of a Serde<T>
   *
   * @param <T> the type for which creating the Serde for.
   */
  public static <T> Function<ConfigurableMapper, StaticSerde<T>> json(
      Function<ConfigurableMapper, JsonEncoder<T>> encoder,
      Function<ConfigurableMapper, JsonDecoder<T>> decoder
  ) {
    return cm -> serde(
        (topic, t) -> encoder.andThen($ -> $.encode(t)).apply(cm),
        (topic, data) -> decoder.andThen($ -> $.decode(data)).apply(cm).getOrElseThrow(
            e -> new RuntimeException(String.format("Unable to deserialize event in topic %s, error: %s", topic, e), e))
    );
  }

//  public static <T> Function<ConfigurableMapper, StaticSerde<T>> json(JsonSerde<T> jsonSerde) {
  public static <T> Function<ConfigurableMapper, StaticSerde<T>> json(JsonSerde<T> jsonSerde) {
    return cm -> serde(
        (topic, t) -> jsonSerde.encoder().andThen($ -> $.encode(t)).apply(cm),
        (topic, data) -> jsonSerde.decoder().andThen($ -> $.decode(data)).apply(cm).getOrElseThrow(
            e -> new RuntimeException(String.format("Unable to deserialize event in topic %s, error: %s", topic, e), e))
    );
  }



  /**
   * Creates an instance of a Serde for `String` type
   * @return a Serde<String>
   */
  public static Function<ConfigurableMapper, StaticSerde<String>> string() {
    return $ -> new StaticSerde<>(Serdes.String());
  }

}
