package fjks.example.model;

import java.time.LocalDateTime;

public class EventWithTimestamp {
    public final LocalDateTime timeOutbound;

    public EventWithTimestamp(LocalDateTime timeOutbound) {
        this.timeOutbound = timeOutbound;
    }
}
