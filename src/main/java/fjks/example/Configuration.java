package fjks.example;

public class Configuration {
    public final String topicName0;
    public final String topicName1;
    public final String detectionTopicName;

    public Configuration(String topicName0, String topicName1, String detectionTopicName) {
        this.topicName0 = topicName0;
        this.topicName1 = topicName1;
        this.detectionTopicName = detectionTopicName;
    }
}
