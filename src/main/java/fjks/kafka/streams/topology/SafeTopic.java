package fjks.kafka.streams.topology;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;

import java.util.function.Function;

public final class SafeTopic<K, V> {
    public final String topicName;
    public final Serde<K> keySerde;
    public final Serde<V> valueSerde;

    public Produced<K, V> asProduced() {
        return Produced.with(keySerde, valueSerde).withName(topicName);
    }

    public Consumed<K, V> asConsumed() {
        return Consumed.with(keySerde, valueSerde).withName(topicName);
    }

//    public static <CFG, K, V> Builder<CFG, K, V> build(
//            Function<CFG, String> name,
//            Serde<K> keySerde,
//            Serde<V> valueSerde
//    ) {
//        return new Builder<>() {
//            @Override
//            public SafeTopic<K, V> apply(CFG cfg) {
//                return new SafeTopic<>(name.apply(cfg), keySerde, valueSerde);
//            }
//        };
//    }

//    public abstract static class Builder<CFG, K, V> implements Function<CFG, SafeTopic<K, V>> {
//    }


    // alternative strategy, using ReaderState.
    public static <CFG, K, V> StreamBuilder<CFG, SafeTopic<K, V>> build(
            final Function<CFG, String> name,
            final Serde<K> keySerde,
            final Serde<V> valueSerde
    ) {

        return StreamBuilder.<CFG>environment().map(cfg -> new SafeTopic<>(name.apply(cfg), keySerde, valueSerde));
    }


    // StreamBuilder<CFG, A>
    private SafeTopic(String topicName, Serde<K> keySerde, Serde<V> valueSerde) {
        this.topicName = topicName;
        this.keySerde = keySerde;
        this.valueSerde = valueSerde;
    }
}
