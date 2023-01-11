package fks4j.kafka.streams.serde.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface ConfigurableMapper {

  ObjectMapper mapper();
}
