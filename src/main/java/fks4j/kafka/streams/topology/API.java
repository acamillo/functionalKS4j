package fks4j.kafka.streams.topology;

import org.apache.kafka.streams.kstream.KStream;

import java.util.Arrays;
import java.util.function.Function;

public final class API   {

    /**
     * Terminal operation. Closes a StreamBuilder pipeline with a 'sink' operation to the specified safe topic
     * @param sb the input StreamBuilder pipeline
     * @return a function whose evaluation terminates the pipeline with a 'sink'
     * @param <CFG>
     * @param <K>
     * @param <V>
     */
    public static <CFG, K,V> Function<KStream<K,V>, StreamBuilder<CFG, Void>> sinkTo(StreamBuilder<CFG, FTopic<K,V>> sb) {
        return kStream -> sb.andThen(sink(kStream));
    }
    public static <K, V> StreamBuilder<FTopic<K, V>, KStream<K, V>> stream() {
        return StreamBuilder.<FTopic<K, V>>environment().flatMap(topic ->
                StreamBuilder.<FTopic<K, V>>get()
                        .map(sb -> sb.stream(topic.topicName, topic.asConsumed())));
    }


    public static <K, V> StreamBuilder<FTopic<K, V>, Void> sink(final KStream<K, V> kStream) {
        return StreamBuilder.<FTopic<K, V>>environment().map(topic -> {
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
    }

    @SafeVarargs
    public static <CFG> StreamBuilder<CFG, Void> combine2(final StreamBuilder<CFG, Void> head,
                                                          final StreamBuilder<CFG, Void>... tail) {
        return Arrays.stream(tail).reduce(head, (acc, elem) -> acc.flatMap($ -> elem));
    }
}
