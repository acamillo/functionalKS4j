package fks4j.kafka.streams.serde.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectWriter;
import fks4j.kafka.streams.serde.JsonEncoder;
import io.vavr.control.Try;
import java.util.function.Function;

public final class JacksonEncoder {

  /**
   * Generates a JsonEncoder instance for type `T` given in input an instance of ConfigurableMapper (i.e an ObjectMapper)
   * @param tRef the type `T` TypeReference. Required by Jackson to properly figure out the generic type `T`
   * @return
   * @param <T>
   */
  public static <T> Function<ConfigurableMapper, JsonEncoder<T>> gen(final TypeReference<T> tRef) {
    return configurableMapper ->
        new JsonEncoderImpl<T>(configurableMapper.mapper().writerFor(tRef)) {
          @Override
          public byte[] encode(T t) {
            return Try.of(() -> writer.writeValueAsBytes(t))
                .getOrElseThrow(e -> new RuntimeException("Unable to encode the value.", e));
          }
        };
  }

  private static abstract class JsonEncoderImpl<T> implements JsonEncoder<T> {

    protected final ObjectWriter writer;

    JsonEncoderImpl(ObjectWriter writer) {
      this.writer = writer;
    }
  }
}
