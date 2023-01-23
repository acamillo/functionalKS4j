package fks4j.kafka.streams.topology;

import fks4j.kafka.streams.serde.jackson.ConfigurableMapper;
import java.time.Duration;
import java.util.Optional;
import java.util.function.Function;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.StreamJoined;
import org.apache.kafka.streams.kstream.ValueJoiner;

public class FJoiner<K, V1, V2> {

  public final Optional<String> mayBeName;
  public final Serde<K> keySerde;
  public final Serde<V1> value1Serde;
  public final Serde<V2> value2Serde;
  public final JoinWindows joinWindow;

  private FJoiner(
      Serde<K> keySerde,
      Serde<V1> value1Serde,
      Serde<V2> value2Serde,
      JoinWindows joinWindow,
      Optional<String> mayBeName) {
    this.mayBeName = mayBeName;
    this.keySerde = keySerde;
    this.value1Serde = value1Serde;
    this.value2Serde = value2Serde;
    this.joinWindow = joinWindow;
  }

  public StreamJoined<K, V1, V2> asStreamJoined() {
    var joiner = StreamJoined.with(keySerde, value1Serde, value2Serde);
    return mayBeName.map(joiner::withName).orElse(joiner);
  }

  public <VR> KStream<K, VR> join(KStream<K, V1> ks1, KStream<K, V2> ks2, ValueJoiner<V1, V2, VR> joiner) {
    return ks1.join(ks2, joiner, joinWindow, asStreamJoined());
  }

  public static <CFG extends ConfigurableMapper, K, V1, V2> StreamBuilder<CFG, FJoiner<K, V1, V2>> of(
      final Function<CFG, Duration> difference,
      final Function<ConfigurableMapper, ? extends ConfigurableSerde<? super CFG, K>> keySerde,
      final Function<ConfigurableMapper, ? extends ConfigurableSerde<? super CFG, V1>> value1Serde,
      final Function<ConfigurableMapper, ? extends ConfigurableSerde<? super CFG, V2>> value2Serde
  ) {
    return StreamBuilder.<CFG>environment().map(cfg ->
        new FJoiner<>(
            keySerde.apply(cfg).serde(true, cfg),
            value1Serde.apply(cfg).serde(false, cfg),
            value2Serde.apply(cfg).serde(false, cfg),
            JoinWindows.ofTimeDifferenceWithNoGrace(difference.apply(cfg)),
            Optional.empty()
        ));
  }

}