package fks4j.kafka.streams.topology;

import fks4j.kafka.streams.serde.jackson.ConfigurableMapper;
import java.util.Optional;
import java.util.function.Function;
import org.apache.kafka.streams.kstream.SessionWindows;
import org.apache.kafka.streams.kstream.SlidingWindows;
import org.apache.kafka.streams.kstream.TimeWindows;

public class FAggregator {

  public static <CFG extends ConfigurableMapper, K, V, VR> StreamBuilder<CFG, SlidingWindowAggregator<K, V, VR>> sliding(
      final StreamBuilder<CFG, SlidingWindows> sliding,
      final Function<ConfigurableMapper, ? extends ConfigurableSerde<? super CFG, K>> keySerde,
      final Function<ConfigurableMapper, ? extends ConfigurableSerde<? super CFG, VR>> valueSerde
  ) {
    return
        sliding.flatMap(window ->
            StreamBuilder.<CFG>environment().map(cfg ->
                new SlidingWindowAggregator<>(
                    window,
                    keySerde.apply(cfg).serde(true, cfg),
                    valueSerde.apply(cfg).serde(false, cfg),
                    Optional.empty()
                )));
  }

  public static <CFG extends ConfigurableMapper, K, V, VR> StreamBuilder<CFG, SessionWindowAggregator<K, V, VR>> session(
      final StreamBuilder<CFG, SessionWindows> session,
      final Function<ConfigurableMapper, ? extends ConfigurableSerde<? super CFG, K>> keySerde,
      final Function<ConfigurableMapper, ? extends ConfigurableSerde<? super CFG, VR>> valueSerde
  ) {
    return
        session.flatMap(window ->
            StreamBuilder.<CFG>environment().map(cfg ->
                new SessionWindowAggregator<>(
                    window,
                    keySerde.apply(cfg).serde(true, cfg),
                    valueSerde.apply(cfg).serde(false, cfg),
                    Optional.empty()
                )));
  }

  public static <CFG extends ConfigurableMapper, K, V, VR> StreamBuilder<CFG, TimeWindowAggregator<K, V, VR>> tumbling(
      final StreamBuilder<CFG, TimeWindows> session,
      final Function<ConfigurableMapper, ? extends ConfigurableSerde<? super CFG, K>> keySerde,
      final Function<ConfigurableMapper, ? extends ConfigurableSerde<? super CFG, VR>> valueSerde
  ) {
    return
        session.flatMap(window ->
            StreamBuilder.<CFG>environment().map(cfg ->
                new TimeWindowAggregator<>(
                    window,
                    keySerde.apply(cfg).serde(true, cfg),
                    valueSerde.apply(cfg).serde(false, cfg),
                    Optional.empty()
                )));
  }
}