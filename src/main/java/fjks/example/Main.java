package fjks.example;

import org.apache.kafka.streams.StreamsBuilder;

public class Main {
    public static void main(String[] args) {
        Configuration cfg = new Configuration("auditd", "diags");
        StreamsBuilder streamsBuilder = null;

//        var sb = new AppTopology(cfg, streamsBuilder).instance();
        var sb =  AppTopology.instance.runS(cfg, streamsBuilder);
//        var sb =  new AppTopology().instance().runS(cfg, streamsBuilder);
    }
}
