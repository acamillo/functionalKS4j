package fks4j.example;

import fks4j.kafka.streams.topology.FWindow;
import fks4j.kafka.streams.topology.StreamBuilder;
import org.apache.kafka.streams.kstream.SessionWindows;
import org.apache.kafka.streams.kstream.SlidingWindows;
import org.apache.kafka.streams.kstream.TimeWindows;

public class Windows {
  static StreamBuilder<Configuration, SlidingWindows> sliding =
      FWindow.sliding(c -> c.aggregationsWindow);
  static StreamBuilder<Configuration, SessionWindows> session =
      FWindow.session(c -> c.aggregationsWindow);
  static StreamBuilder<Configuration, TimeWindows> tumbling =
      FWindow.tumbling(c -> c.aggregationsWindow);

}
