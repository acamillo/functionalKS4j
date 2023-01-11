package fks4j.example;

import fks4j.kafka.streams.topology.FJoinWindow;
import fks4j.kafka.streams.topology.StreamBuilder;

public class Windows {
    public final static StreamBuilder<Configuration, org.apache.kafka.streams.kstream.JoinWindows> topic0_1Window =
            FJoinWindow.from(c->c.joinWindow);
}
