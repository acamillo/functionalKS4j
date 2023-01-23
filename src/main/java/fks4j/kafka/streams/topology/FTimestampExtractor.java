package fks4j.kafka.streams.topology;

import java.util.function.ToLongFunction;

class FTimestampExtractor {
    @SuppressWarnings("unchecked")
    public  static <T> org.apache.kafka.streams.processor.TimestampExtractor from(ToLongFunction<T> extractor) {
        return (consumerRecord, l) -> extractor.applyAsLong((T)consumerRecord.value());
    }

}
