package fks4j.kafka.streams.serde.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectReader;
import fks4j.kafka.streams.serde.JsonDecoder;
import io.vavr.control.Either;
import io.vavr.control.Try;
import java.util.function.Function;

public final class JacksonDecoder {

  public static <T> Function<ConfigurableMapper, JsonDecoder<T>> gen(final TypeReference<T> tRef) {
    return configurableMapper ->
        new JsonDecoderImpl<T>(configurableMapper.mapper().readerFor(tRef)) {
          @Override
          public Either<Throwable, T> decode(byte[] data) {
            return Try.of(() -> reader.<T>readValue(data)).toEither();
          }
        };
  }

  private static abstract class JsonDecoderImpl<T> implements JsonDecoder<T> {

    protected final ObjectReader reader;

    JsonDecoderImpl(ObjectReader reader) {
      this.reader = reader;
    }
  }
}
