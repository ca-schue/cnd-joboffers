package thi.cnd.careerservice.setup.container;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@Component
@Profile("integrationtest")
public class KafkaDockerContainer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaDockerContainer.class);

    private static final KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

    public static CompletableFuture<Void> start() {
        return CompletableFuture.runAsync(() -> {
            try {
                logger.info("Starting Kafka docker test container");
                kafka.start();
            } catch (Exception e) {
                logger.info("Could not start Kafka docker test container", e);
                throw e;
            }
        });
    }

    public static void stop() {
        kafka.stop();
    }

    public static boolean isRunning() {
        return kafka.isRunning();
    }

    public static String getBootstrapServerUrl() {
        return kafka.getHost() + ":" + kafka.getFirstMappedPort();
    }
}
