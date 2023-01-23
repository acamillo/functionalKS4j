package fks4j.kafka.streams.topology;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.apache.kafka.streams.StreamsBuilder;

public final class StreamBuilder<E, A> {

    public BiFunction<E, StreamsBuilder, Tuple2<StreamsBuilder, A>> run;

    private StreamBuilder(BiFunction<E, StreamsBuilder, Tuple2<StreamsBuilder, A>> run) {
        this.run = run;
    }

    public static <CFG> StreamBuilder<CFG, CFG> environment() {
        return new StreamBuilder<>((cfg, s) -> new Tuple2<>(s, cfg));
    }

    public <B> StreamBuilder<E, B> map(final Function<A, B> f) {
        return new StreamBuilder<>((env, sa) -> {
            var res = run.apply(env, sa);
            return new Tuple2<>(res._1, f.apply(res._2));
        });
    }

    public static <CFG> StreamBuilder<CFG, StreamsBuilder> get() {
        return new StreamBuilder<>((env, sa) -> Tuple.of(sa, sa));
    }

    /**
     * Modify the result of the computation by feeding it into `f`, threading the state
     * through the resulting computation.
     */
    public <B> StreamBuilder<E, B> flatMap(final Function<A, StreamBuilder<E, B>> f) {
        return new StreamBuilder<>((env, sa) -> {
            var res = run.apply(env, sa);
            return f.apply(res._2).run.apply(env, res._1);
        });
    }

    public <B> StreamBuilder<E, B> andThen(final StreamBuilder<A, B> that) {
        return new StreamBuilder<>((env, sa) -> {
            var res = run.apply(env, sa);
            return that.run.apply(res._2(), res._1());
        });
    }


    /**
     * Run the computation using the provided initial environment and state, but discards the final value.
     */
    public StreamsBuilder runS(final E env, final StreamsBuilder initial) {
        return run.apply(env, initial)._1;
    }

}