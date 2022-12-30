package fjks.kafka.streams.topology;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.apache.kafka.streams.StreamsBuilder;

import java.util.function.Function;

public final class StreamBuilder<E, A> { //extends ReaderState<CFG, StreamsBuilder, StreamsBuilder, A> {

//    public BiFunction<E, StreamsBuilder, Tuple2<StreamsBuilder, A>> run;
    public final Function<Tuple2<E, StreamsBuilder>, Tuple2<StreamsBuilder, A>> run;

//    private StreamBuilder(BiFunction<E, StreamsBuilder, Tuple2<StreamsBuilder, A>> run) {
    private StreamBuilder(final Function<Tuple2<E, StreamsBuilder>, Tuple2<StreamsBuilder, A>> run) {
//        super(run);
        this.run = run;
    }

    public static <CFG> StreamBuilder<CFG, CFG> environment() {
//        return new StreamBuilder<>((cfg, s) -> new Tuple2<>(s, cfg));
        return new StreamBuilder<>(Tuple2::swap);
    }

    public <B> StreamBuilder<E, B> map(final Function<A, B> f) {
        return new StreamBuilder<>(tuple2 -> {
//        return new StreamBuilder<>((env, sa) -> {
//            var res = run.apply(env, sa);
            var res = run.apply(tuple2);
//            return new Tuple2<>(res._1, f.apply(res._2));
            return Tuple.of(res._1, f.apply(res._2));
        });

//        return (StreamBuilder<CFG, B>) super.map(f);
    }

    public static <CFG> StreamBuilder<CFG, StreamsBuilder> get() {
        return new StreamBuilder<>(tuple2 -> Tuple.of(tuple2._2, tuple2._2));
    }

    /**
     * Modify the result of the computation by feeding it into `f`, threading the state
     * through the resulting computation.
     */
//    public <B> StreamBuilder<E, B> flatMap(Function<A, StreamBuilder<E, B>> f) {
//        return new StreamBuilder<>((env, sa) -> {
//            var res = run.apply(env, sa);
//            return f.apply(res._2).run.apply(env, res._1);
//        });
//    }

    public <B> StreamBuilder<E, B> flatMap(final Function<A, StreamBuilder<E, B>> f) {
        return new StreamBuilder<>(t2 -> {
            var res = run.apply(t2);
            return f.apply(res._2).run.apply(Tuple.of(t2._1, res._1));
        });
    }

//    public <B> StreamBuilder<E, B> andThen(StreamBuilder<A, B> that) {
//        return new StreamBuilder<>((env, sa) -> {
//            var res = run.apply(env, sa);
//            return that.run.apply(res._2(), res._1());
//        });
//    }

    public <B> StreamBuilder<E, B> andThen(final StreamBuilder<A, B> that) {
        return new StreamBuilder<>(t2 -> {
            var res = run.apply(t2);
            return that.run.apply(res.swap());
        });
    }

    /**
     * Run the computation using the provided initial environment and state.
     */
    public Tuple2<StreamsBuilder, A> run(final E env, final StreamsBuilder initial) {
//        return run.apply(env, initial);
        return run.apply(Tuple.of(env, initial));
    }

    /**
     * Like [[run]], but discards the final value.
     */
    public StreamsBuilder runS(final E env, final StreamsBuilder initial) {
        return run(env, initial)._1;
    }

}