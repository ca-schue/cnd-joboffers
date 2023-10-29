package thi.cnd.careerservice.setup.container;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@Component
@Profile("integrationtest")
public class EventStoreDBDockerContainer {

    private static final Logger logger = LoggerFactory.getLogger(EventStoreDBDockerContainer.class);

    private static final GenericContainer<?> eventStoreDB = new GenericContainer<>(DockerImageName.parse("eventstore/eventstore:23.6.0-buster-slim"))
        .withExposedPorts(1113, 2113)
        .withEnv(
            Map.of(
                "EVENTSTORE_CLUSTER_SIZE", "1",
                "EVENTSTORE_RUN_PROJECTIONS", "All",
                "EVENTSTORE_START_STANDARD_PROJECTIONS", "true",
                "EVENTSTORE_EXT_TCP_PORT", "1113",
                "EVENTSTORE_HTTP_PORT", "2113",
                "EVENTSTORE_INSECURE", "true",
                "EVENTSTORE_ENABLE_EXTERNAL_TCP", "true",
                "EVENTSTORE_ENABLE_ATOM_PUB_OVER_HTTP", "true"
            )
        );

    public static CompletableFuture<Void> start() {
        return CompletableFuture.runAsync(() -> {
            try {
                logger.info("Starting EventStoreDB docker test container");
                eventStoreDB.start();
            } catch (Exception e) {
                logger.info("Could not start EventStoreDB docker test container", e);
                throw e;
            }
        });
    }

    public static void stop() {
        eventStoreDB.stop();
    }

    public static boolean isRunning() {
        return eventStoreDB.isRunning();
    }

    public static Integer getMappedPort(int port) {
        return eventStoreDB.getMappedPort(port);
    }
}
