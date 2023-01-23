package fks4j.kafka.streams.topology;

import java.util.Optional;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.kstream.Aggregator;
import org.apache.kafka.streams.kstream.Initializer;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.state.WindowStore;

public record TimeWindowAggregator<K, V, VA>(
    TimeWindows window,
    Serde<K> keySerde,
    Serde<VA> aggregateSerde,
    Optional<String> mayBeName) {

  public KTable<Windowed<K>, VA> aggregateByKey(
      final KStream<K, V> kStream,
      final Initializer<VA> initializer,
      final Aggregator<? super K, ? super V, VA> aggregator) {

    return kStream
        .groupByKey()
        .windowedBy(window)
        .aggregate(initializer, aggregator, asMaterialized());
  }

  public Materialized<K, VA, WindowStore<Bytes, byte[]>> asMaterialized() {
    return mayBeName
        .map(name -> Materialized.<K, VA, WindowStore<Bytes, byte[]>>as(name).withKeySerde(keySerde).withValueSerde(aggregateSerde))
        .orElse(Materialized.with(keySerde, aggregateSerde));
  }

}