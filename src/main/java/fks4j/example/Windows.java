package fks4j.example;

import fks4j.kafka.streams.topology.SafeJoinWindow;
import fks4j.kafka.streams.topology.StreamBuilder;

public class Windows {
    public final static StreamBuilder<Configuration, org.apache.kafka.streams.kstream.JoinWindows> topic0_1Window =
            SafeJoinWindow.of(c->c.joinWindow);
}
