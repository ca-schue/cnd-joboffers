package thi.cnd.careerservice.setup.container;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

@Component
@Profile("integrationtest")
public class MongoDBDockerContainer {

    private static final Logger logger = LoggerFactory.getLogger(MongoDBDockerContainer.class);

    private static final MongoDBContainer mongoDB = new MongoDBContainer(DockerImageName.parse("mongo"));

    public static CompletableFuture<Void> start() {
        return CompletableFuture.runAsync(() -> {
            try {
                logger.info("Starting MongoDB docker test container");
                mongoDB.start();
            } catch (Exception e) {
                logger.info("Could not start MongoDB docker test container", e);
                throw e;
            }
        });
    }

    public static void stop() {
        mongoDB.stop();
    }

    public static boolean isRunning() {
        return mongoDB.isRunning();
    }

    public static String getConnectionString() {
        return mongoDB.getConnectionString() + "/career-service?authSource=admin&readPreference=primary&directConnection=true&ssl=false";
    }
}
