package fks4j.kafka.streams.topology;

import org.apache.kafka.streams.kstream.JoinWindows;

import java.time.Duration;
import java.util.function.Function;

public class SafeJoinWindow {
    public static <CFG> StreamBuilder<CFG, JoinWindows> of(
            final Function<CFG, Duration> difference){
        return StreamBuilder.<CFG>environment().map(cfg ->  JoinWindows.ofTimeDifferenceWithNoGrace(difference.apply(cfg)));
    }

    public static <CFG> StreamBuilder<CFG, JoinWindows> of(
            final Function<CFG, Duration> difference,
            final Function<CFG, Duration> grace) {
        return StreamBuilder.<CFG>environment().map(cfg -> JoinWindows.ofTimeDifferenceAndGrace(difference.apply(cfg), grace.apply(cfg)));
    }

}
