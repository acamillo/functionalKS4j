package fks4j.kafka.streams.topology;

import fks4j.kafka.streams.serde.jackson.ConfigurableMapper;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.ToLongFunction;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.processor.TimestampExtractor;

public final class FTopic<K, V> {

  public final String topicName;
  public final Serde<K> keySerde;
  public final Serde<V> valueSerde;

  private final Optional<TimestampExtractor> mayBeExtractor;

  public Produced<K, V> asProduced() {
    return Produced.with(keySerde, valueSerde).withName(topicName);
  }

  public Consumed<K, V> asConsumed() {
    return mayBeExtractor.map(
            extractor -> Consumed.with(keySerde, valueSerde).withName(topicName).withTimestampExtractor(extractor))
        .orElse(Consumed.with(keySerde, valueSerde).withName(topicName));
  }

//  public static <CFG, K, V> StreamBuilder<CFG, FTopic<K, V>> of(
//      final Function<CFG, String> name,
//      final Serde<K> keySerde,
//      final Serde<V> valueSerde
//  ) {
//    return StreamBuilder.<CFG>environment()
//        .map(cfg -> new FTopic<>(name.apply(cfg), keySerde, valueSerde, Optional.empty()));
//  }


  //  public static <CFG, K, V> StreamBuilder<CFG, FTopic<K, V>> of3(
//      final Function<CFG, String> name,
//      final Function<CFG, ObjectMapper> mapper,
//      final Function<ObjectMapper, ? extends ConfigurableSerde<? super CFG, K>> keySerde,
//      final Function<ObjectMapper, ? extends ConfigurableSerde<? super CFG, V>> valueSerde
//  ) {
//    return StreamBuilder.<CFG>environment().map(cfg -> new FTopic<>(
//        name.apply(cfg),
//        mapper.andThen(keySerde.andThen(ConfigurableSerde::serde)).apply(cfg).apply(true, cfg),
//        mapper.andThen(valueSerde.andThen(ConfigurableSerde::serde)).apply(cfg).apply(false, cfg),
//        Optional.empty()
//    ));
//  }
  public static <CFG extends ConfigurableMapper, K, V> StreamBuilder<CFG, FTopic<K, V>> from(
      final Function<CFG, String> name,
      final Function<ConfigurableMapper, ? extends ConfigurableSerde<? super CFG, K>> keySerde,
      final Function<ConfigurableMapper, ? extends ConfigurableSerde<? super CFG, V>> valueSerde
  ) {
    return StreamBuilder.<CFG>environment().map(cfg ->
        new FTopic<>(
            name.apply(cfg),
            keySerde.apply(cfg).serde(true, cfg),
            valueSerde.apply(cfg).serde(false, cfg),
//        keySerde.andThen(ConfigurableSerde::serde).apply(cfg).apply(true, cfg),
//        valueSerde.andThen(ConfigurableSerde::serde).apply(cfg).apply(false, cfg),
            Optional.empty()
        ));
  }

  public static <CFG extends ConfigurableMapper, K, V> StreamBuilder<CFG, FTopic<K, V>> from(
      final Function<CFG, String> name,
      final Function<ConfigurableMapper, ? extends ConfigurableSerde<? super CFG, K>> keySerde,
      final Function<ConfigurableMapper, ? extends ConfigurableSerde<? super CFG, V>> valueSerde,
      final ToLongFunction<V> extractor
  ) {
    return StreamBuilder.<CFG>environment().map(cfg ->
        new FTopic<>(name.apply(cfg),
            keySerde.apply(cfg).serde(true, cfg),
            valueSerde.apply(cfg).serde(false, cfg),
            Optional.of(extractor)));
  }

  private FTopic(String topicName, Serde<K> keySerde, Serde<V> valueSerde, Optional<ToLongFunction<V>> mayBeExtractor) {
    this.topicName = topicName;
    this.keySerde = keySerde;
    this.valueSerde = valueSerde;
    this.mayBeExtractor = mayBeExtractor.map(FTimestampExtractor::from);
  }
}
