package fks4j.kafka.streams.topology;

import java.util.Optional;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.kstream.Aggregator;
import org.apache.kafka.streams.kstream.Initializer;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Merger;
import org.apache.kafka.streams.kstream.SessionWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.state.SessionStore;

public record SessionWindowAggregator<K, V, VA>(
    SessionWindows window,
    Serde<K> keySerde,
    Serde<VA> aggregateSerde,
    Optional<String> mayBeName) {

  public KTable<Windowed<K>, VA> aggregateByKey(
      final KStream<K, V> kStream,
      final Initializer<VA> initializer,
      final Aggregator<? super K, ? super V, VA> aggregator,
      final Merger<? super K, VA> sessionMerger) {

    return kStream
        .groupByKey()
        .windowedBy(window)
        .aggregate(initializer, aggregator, sessionMerger, asMaterialized());
  }

//  public <KR> KTable<Windowed<KR>, VR> aggregateBy(
//      KStream<K, V> ks1,
//      final KeyValueMapper<? super K, ? super V, KR> mapper,
//      final Initializer<VR> initializer,
//      final Aggregator<? super KR, ? super V, VR> aggregator,
//      final Merger<? super KR, VR> sessionMerger) {
//
//    // final Materialized<K, VR, SessionStore<Bytes, byte[]>> materialized);
//    return ks1.groupBy(mapper).windowedBy(window)
//        .aggregate(initializer, aggregator, sessionMerger,
//            null,
//            Materialized.<KR, VR, SessionStore<Bytes, byte[]>>as("topicforchangelog")
//                .withKeySerde(keySerde)
//                .withValueSerde(aggregateSerde)
//        );
//  }

  public Materialized<K, VA, SessionStore<Bytes, byte[]>> asMaterialized() {
    return mayBeName
        .map(name -> Materialized.<K, VA, SessionStore<Bytes, byte[]>>as(name).withKeySerde(keySerde).withValueSerde(aggregateSerde))
        .orElse(Materialized.with(keySerde, aggregateSerde));

//    return Materialized.<K, VA, SessionStore<Bytes, byte[]>>as("topicforchangelog")
//        .withKeySerde(keySerde)
//        .withValueSerde(aggregateSerde);
//        .withCachingDisabled();
  }

}