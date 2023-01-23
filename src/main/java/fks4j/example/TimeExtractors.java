package fks4j.example;

import fks4j.example.model.ModelD;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.function.ToLongFunction;

class TimeExtractors {

  final static ZoneId zone = ZoneId.of("America/Los_Angeles");
  final static ToLongFunction<ModelD> modelDExtractor =
//  event -> ZonedDateTime.of(event.timestamp(), zone).toEpochSecond() * 1000;
//      event -> event.timestamp().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
      event -> event.timestamp().toInstant(java.time.ZoneOffset.UTC).toEpochMilli();


  static long toMillis(final LocalDateTime ldt) {
    return ldt.toInstant(java.time.ZoneOffset.UTC).toEpochMilli();
  }
}