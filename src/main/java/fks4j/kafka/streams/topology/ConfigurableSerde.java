package fks4j.kafka.streams.topology;

import org.apache.kafka.common.serialization.Serde;

public interface ConfigurableSerde<CFG, T> {
//    BiFunction<Boolean, ? super CFG, Serde<T>> serde();
     Serde<T> serde(Boolean isKey, CFG cfg);
}
