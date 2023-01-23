package fks4j.kafka.streams.topology;

import java.time.Duration;
import java.util.function.Function;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.SessionWindows;
import org.apache.kafka.streams.kstream.SlidingWindows;
import org.apache.kafka.streams.kstream.TimeWindows;

public class FWindow {
    public static <CFG> StreamBuilder<CFG, JoinWindows> from(
            final Function<CFG, Duration> difference){
        return StreamBuilder.<CFG>environment().map(cfg ->  JoinWindows.ofTimeDifferenceWithNoGrace(difference.apply(cfg)));
    }

    public static <CFG> StreamBuilder<CFG, JoinWindows> from(
            final Function<CFG, Duration> difference,
            final Function<CFG, Duration> grace) {
        return StreamBuilder.<CFG>environment().map(cfg -> JoinWindows.ofTimeDifferenceAndGrace(difference.apply(cfg), grace.apply(cfg)));
    }

    public static <CFG> StreamBuilder<CFG, SessionWindows> session(
        final Function<CFG, Duration> inactivityGap) {
        return StreamBuilder.<CFG>environment().map(cfg -> SessionWindows.ofInactivityGapWithNoGrace((inactivityGap.apply(cfg))));
    }

    public static <CFG> StreamBuilder<CFG, SlidingWindows> sliding(
        final Function<CFG, Duration> timeDifference) {
        return StreamBuilder.<CFG>environment().map(cfg -> SlidingWindows.ofTimeDifferenceWithNoGrace((timeDifference.apply(cfg))));
    }

    public static <CFG> StreamBuilder<CFG, TimeWindows> tumbling(
        final Function<CFG, Duration> size) {
        return StreamBuilder.<CFG>environment().map(cfg -> TimeWindows.ofSizeWithNoGrace(size.apply(cfg)));
    }
}
