package fks4j.kafka.streams.serde;

import io.vavr.control.Either;

public interface JsonDecoder<T> {
    Either<Throwable, T> decode(byte[] data);
}
