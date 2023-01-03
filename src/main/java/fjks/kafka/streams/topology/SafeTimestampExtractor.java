package fjks.kafka.streams.topology;

import java.lang.reflect.ParameterizedType;
import java.util.function.ToLongFunction;

class SafeTimestampExtractor<T> {
    private final Class<T> clazz;

    @SuppressWarnings("unchecked")
    public SafeTimestampExtractor() {
        // read here for this reflection dark magic: https://stackoverflow.com/questions/4837190/java-generics-get-class
        clazz  = (Class<T>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public  org.apache.kafka.streams.processor.TimestampExtractor of(ToLongFunction<T> extractor) {
        return (consumerRecord, l) -> extractor.applyAsLong(clazz.cast(consumerRecord.value()));
    }

//    public  org.apache.kafka.streams.processor.TimestampExtractor of(Class<T> clazz, ToLongFunction<T> extractor) {
//        return (consumerRecord, l) -> extractor.applyAsLong(clazz.cast(consumerRecord.value()));
//    }

}
