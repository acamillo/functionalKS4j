package fjks.kafka.streams.topology;

import org.apache.kafka.streams.kstream.KStream;

import java.util.Arrays;
import java.util.function.Function;

public final class KStreamSdk {

    /**
     * Terminal operation. Closes a StreamBuilder pipeline with a 'sink' operation to the specified safe topic
     * @param sb the input StreamBuilder pipeline
     * @return a function whose evaluation terminates the pipeline with a 'sink'
     * @param <CFG>
     * @param <K>
     * @param <V>
     */
    public static <CFG, K,V> Function<KStream<K,V>, StreamBuilder<CFG, Void>> sinkTo(StreamBuilder<CFG, SafeTopic<K,V>> sb) {
        return kStream -> sb.andThen(sink(kStream));
    }
    public static <K, V> StreamBuilder<SafeTopic<K, V>, KStream<K, V>> stream() {
        return StreamBuilder.<SafeTopic<K, V>>environment().flatMap(topic ->
                StreamBuilder.<SafeTopic<K, V>>get()
                        .map(sb -> sb.stream(topic.topicName, topic.asConsumed())));
    }


    public static <K, V> StreamBuilder<SafeTopic<K, V>, Void> sink(final KStream<K, V> kStream) {
        return StreamBuilder.<SafeTopic<K, V>>environment().map(topic -> {
            kStream.to(topic.topicName, topic.asProduced());
            return null;
        });

    }

    /**
     * Composes a stream computation with a 'sink' terminal operation threading the state through the resulting computation.
     *
     * @param gen  the stream generator
     * @param sink a terminal operation
     */
    public static <CFG, K0, V0> StreamBuilder<CFG, Void> compose(
            final StreamBuilder<CFG, KStream<K0, V0>> gen,
            final Function<KStream<K0, V0>, StreamBuilder<CFG, Void>> sink
    ) {
        return gen.flatMap(sink);
    }

    /**
     * Composes two dependant stream computations, and 'sink' terminal operation
     * threading the state through the resulting computation.
     *
     * @param gen  the stream generator
     * @param sb1  a dependant stream computation
     * @param sink a terminal operation
     */
    public static <CFG, K0, V0, K1, V1> StreamBuilder<CFG, Void> compose(
            final StreamBuilder<CFG, KStream<K0, V0>> gen,
            final Function<KStream<K0, V0>, StreamBuilder<CFG, KStream<K1, V1>>> sb1,
            final Function<KStream<K1, V1>, StreamBuilder<CFG, Void>> sink
    ) {
        return gen.flatMap(sb1).flatMap(sink);
    }

    /**
     * Composes three dependant stream computations, and 'sink' terminal operation
     * threading the state through the resulting computation.
     *
     * @param gen  the stream generator
     * @param sb1  a dependant stream computation (gen -> sb1)
     * @param sb2  a dependant stream computation (sb1 -> sb2)
     * @param sink a terminal operation  (sb2 -> sink)
     */
    public static <CFG, K0, V0, K1, V1, K2, V2> StreamBuilder<CFG, Void> compose(
            final StreamBuilder<CFG, KStream<K0, V0>> gen,
            final Function<KStream<K0, V0>, StreamBuilder<CFG, KStream<K1, V1>>> sb1,
            final Function<KStream<K1, V1>, StreamBuilder<CFG, KStream<K2, V2>>> sb2,
            final Function<KStream<K2, V2>, StreamBuilder<CFG, Void>> sink
    ) {
        return gen.flatMap(sb1).flatMap(sb2).flatMap(sink);
    }

    /**
     * Combine terminated a list of stream computations
     *
     * @param topologies
     * @return
     */
    @SafeVarargs
    public static <CFG> StreamBuilder<CFG, Void> combine(final StreamBuilder<CFG, Void>... topologies) {
        return Arrays.stream(topologies)
                .reduce((acc, elem) -> acc.flatMap($ -> elem))
                .orElseThrow(() -> new IllegalArgumentException("combine requires a minimum of two elements"));

//        return Arrays.stream(topologies).skip(1).reduce(topologies[0],
//                (acc, elem) -> acc.flatMap($ -> elem));
    }
}
