package fks4j.example;

import java.time.Duration;
import java.util.Properties;

public class Configuration {
    public final String inputTopic0;
    public final String inputTopic1;
    public final String outputTopic0;

    public final Duration joinWindow;
    public final Properties properties;

    public Configuration(String inputTopic0, String inputTopic1, String outputTopic0, Duration joinWindow, Properties properties) {
        this.inputTopic0 = inputTopic0;
        this.inputTopic1 = inputTopic1;
        this.outputTopic0 = outputTopic0;
        this.properties = properties;
        this.joinWindow = joinWindow;
    }
}
