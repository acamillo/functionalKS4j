package fks4j.example;

import fks4j.example.model.EventWithTimestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.function.ToLongFunction;

class TimeExtractors {

  final static ZoneId zone = ZoneId.of("America/Los_Angeles");
  final static ToLongFunction<EventWithTimestamp> eventWithTimestamp =
      event -> ZonedDateTime.of(event.timeOutbound(), zone).toEpochSecond() * 1000;
}