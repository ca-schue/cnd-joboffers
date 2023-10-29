package thi.cnd.careerservice.setup.container;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;
import org.springframework.test.context.DynamicPropertyRegistry;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Profile("integrationtest")
public class TestContainerManager {

    private static final Logger logger = LoggerFactory.getLogger(TestContainerManager.class);

    public static synchronized void startAll() {
        logger.info("Starting docker test containers");
        var startEventStoreDB = EventStoreDBDockerContainer.start();
        var startKafka = KafkaDockerContainer.start();
        var startMongoDB =  MongoDBDockerContainer.start();

        try {
            CompletableFuture.allOf(startEventStoreDB, startKafka, startMongoDB).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Could not start integration test containers", e);
        } catch (ExecutionException e) {
            throw new RuntimeException("Could not start integration test containers", e);
        }
    }

    public static void overrideApplicationProperties(DynamicPropertyRegistry registry) {
        setKafkaProperties(registry);
        setEventStoreDBProperties(registry);
        setMongoDBProperties(registry);
    }

    private static void setKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", KafkaDockerContainer::getBootstrapServerUrl);
        registry.add("spring.kafka.admin.properties.min-in-sync-replicas", () -> 1);
        registry.add("spring.kafka.admin.properties.number-of-replicas", () -> 1);
    }

    private static void setEventStoreDBProperties(DynamicPropertyRegistry registry) {
        registry.add("event.event-store-uri", () -> "esdb://localhost:" + EventStoreDBDockerContainer.getMappedPort(2113) + "?tls=false");
    }

    private static void setMongoDBProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", MongoDBDockerContainer::getConnectionString);
    }

}
