package fks4j.kafka.streams.serde.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import fks4j.kafka.streams.serde.JsonDecoder;
import fks4j.kafka.streams.serde.JsonEncoder;
import java.util.function.Function;

public record JsonSerde<T>(
    Function<ConfigurableMapper, JsonEncoder<T>> encoder,
    Function<ConfigurableMapper, JsonDecoder<T>> decoder
) {

  public static <T> JsonSerde<T> gen(final TypeReference<T> tRef) {
    return new JsonSerde<T>(JacksonEncoder.gen(tRef), JacksonDecoder.gen(tRef));
  }
}