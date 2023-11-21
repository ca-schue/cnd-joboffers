package thi.cnd.userservice.adapters.in.event.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class KafkaConfig {

    public static String OVERRIDE_KAFKA_BOOTSTRAP_ADDRESS = null;

    private final String bootstrapAddress;

    public KafkaConfig(@Value("${spring.kafka.bootstrap-servers}") String bootstrapAddress) {
        this.bootstrapAddress = bootstrapAddress;
    }

}
