package fjks.example;

import org.apache.kafka.streams.StreamsBuilder;

public class Main {
    public static void main(String[] args) {
        Configuration cfg = new Configuration("auditd", "diags");
        StreamsBuilder streamsBuilder = null;

//        var sb = new AppTopology(cfg, streamsBuilder).instance();
        var sb0 =  AppTopology.topology.runS(cfg, streamsBuilder);

        var sb1 = AppTopology2.INSTANCE.topology.runS(cfg, streamsBuilder);
        var sb2 = AppTopology3.INSTANCE.topology.runS(cfg, streamsBuilder);
//        var sb =  new AppTopology().instance().runS(cfg, streamsBuilder);
    }
}
