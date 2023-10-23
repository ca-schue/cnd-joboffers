package thi.cnd.userservice.secondary.event.config;

import lombok.Getter;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.config.TopicBuilder;

import java.util.HashMap;
import java.util.Map;

@Getter
@EnableConfigurationProperties
@ConfigurationProperties("event.kafka")
public class KafkaTopicConfig {

    private final Map<String, TopicConfig> topics = new HashMap<>();
    public record TopicConfig(String name, int partitions, int replicas) {
        public NewTopic toTopic() {
            return TopicBuilder.name(name())
                    .partitions(partitions())
                    .replicas(replicas())
                    .build();
        }
    }
}
