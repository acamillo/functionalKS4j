package fks4j.kafka.streams.topology;

import io.vavr.Tuple2;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Represents a stateful computation from state `SA` to state `SB`,
 * with an initial environment `E` and a result `A`.
 * <p>
 * In other words, it is a pre-baked stack of `[[ReaderT]][F, E, A]`, `[[WriterT]][F, L, A]`
 * and `[[IndexedStateT]][F, SA, SB, A]`.
 */
public class ReaderState<E, SA, SB, A> {
    public BiFunction<E, SA, Tuple2<SB, A>> run;

    public <B> ReaderState<E, SA, SB, B> map(Function<A, B> f) {
        return new ReaderState<>((e, sa) -> {
            var res = run.apply(e, sa);
            return new Tuple2<>(res._1, f.apply(res._2));
        });
    }

//    public static <E, SA, SB, A, B> Function<ReaderState<E, SA, SB, A>, ReaderState<E, SA, SB, B>> map2(Function<A, B> f) {
//        return in -> new ReaderState<>((e, sa) -> {
//            var res = in.run.apply(e, sa);
//            return new Tuple2<>(res._1, f.apply(res._2));
//        });
//    }


    public <SC, B> ReaderState<E, SA, SC, B> flatMap(Function<A, ReaderState<E, SB, SC, B>> f) {
        return new ReaderState<>((e, sa) -> {
            var res = run.apply(e, sa);
            return f.apply(res._2()).run.apply(e, res._1());
        });
    }

    public <SC, B> ReaderState<E, SA, SC, B> andThen(ReaderState<A, SB, SC, B> that) {
        return new ReaderState<>((e, sa) -> {
            var res = run.apply(e, sa);
            return that.run.apply(res._2(), res._1());
        });
    }

    /**
     * Run the computation using the provided initial environment and state.
     */
    public Tuple2<SB, A> run(E env, SA initial) {
        return run.apply(env, initial);
    }

    /**
     * Like [[run]], but discards the final value and log.
     */
    public SB runS(E env, SA initial) {
        return run(env, initial)._1;
    }

    /**
     * Like [[run]], but discards the final state and log.
     */
    public A runA(E env, SA initial) {
        return run(env, initial)._2;
    }

    /**
     * Modify the input state using `f`.
     */
    public ReaderState<E, SA, SB, Void> modify(Function<SA, SB> f) {
        return new ReaderState<>((ign, s) -> new Tuple2<>(f.apply(s), null));
    }

    /**
     * Return the input state without modifying it.
     */
    public static <E, S> ReaderState<E, S, S, S> get() {
        return new ReaderState<>((ign, s) -> new Tuple2<>(s, s));
    }
    /**
     * Set the state to `s`.
     */
    public static <E, S> ReaderState<E, S, S, Void> set(S s) {
        return new ReaderState<>((ign0, ign1) -> new Tuple2<>(s, null));
    }

    /**
     * Get the provided environment, without modifying the input state.
     */
    public static <E, S> ReaderState<E, S, S, E> ask() {
        return new ReaderState<>((e, s) -> new Tuple2<>(s, e));
    }

    public ReaderState(BiFunction<E, SA, Tuple2<SB, A>> run) {
        this.run = run;
    }
}

