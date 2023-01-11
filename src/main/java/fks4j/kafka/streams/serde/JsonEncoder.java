package fks4j.kafka.streams.serde;

public interface JsonEncoder<T> {

     byte[] encode(T t);
}
