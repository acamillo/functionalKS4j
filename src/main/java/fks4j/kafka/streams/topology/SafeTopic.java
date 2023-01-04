package fks4j.kafka.streams.topology;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.processor.TimestampExtractor;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.ToLongFunction;

public final class SafeTopic<K, V> {
    public final String topicName;
    public final Serde<K> keySerde;
    public final Serde<V> valueSerde;

    private final Optional<TimestampExtractor> mayBeExtractor;

    public Produced<K, V> asProduced() {
        return Produced.with(keySerde, valueSerde).withName(topicName);
    }

    public Consumed<K, V> asConsumed() {
        return mayBeExtractor.map(extractor -> Consumed.with(keySerde, valueSerde).withName(topicName).withTimestampExtractor(extractor))
                .orElse(Consumed.with(keySerde, valueSerde).withName(topicName));
    }

    public static <CFG, K, V> StreamBuilder<CFG, SafeTopic<K, V>> of(
            final Function<CFG, String> name,
            final Serde<K> keySerde,
            final Serde<V> valueSerde
    ) {
        return StreamBuilder.<CFG>environment().map(cfg -> new SafeTopic<>(name.apply(cfg), keySerde, valueSerde, Optional.empty()));
    }

    public static <CFG, K, V> StreamBuilder<CFG, SafeTopic<K, V>> of(
            final Function<CFG, String> name,
            final Serde<K> keySerde,
            final Serde<V> valueSerde,
            final ToLongFunction<V> extractor
    ) {
        return StreamBuilder.<CFG>environment().map(cfg -> new SafeTopic<>(name.apply(cfg), keySerde, valueSerde, Optional.of(extractor)));
    }

    private SafeTopic(String topicName, Serde<K> keySerde, Serde<V> valueSerde, Optional<ToLongFunction<V>> mayBeExtractor) {
        this.topicName = topicName;
        this.keySerde = keySerde;
        this.valueSerde = valueSerde;
        this.mayBeExtractor = mayBeExtractor.map(extractor -> new SafeTimestampExtractor<V>().of(extractor));
    }
}
