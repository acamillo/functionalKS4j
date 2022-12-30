package fjks.example;

public class Configuration {
    public final String auditdTopicName;
    public final String detectionTopicName;

    public Configuration(String auditdTopicName, String detectionTopicName) {
        this.auditdTopicName = auditdTopicName;
        this.detectionTopicName = detectionTopicName;
    }
}
