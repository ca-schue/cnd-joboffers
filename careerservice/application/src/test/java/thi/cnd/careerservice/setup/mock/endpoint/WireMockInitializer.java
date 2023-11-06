package thi.cnd.careerservice.setup.mock.endpoint;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextClosedEvent;

import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

/**
 * Creates a {@link WireMockServer} instance and make it available for the integration test application context.
 */
@Profile("integrationtest")
public class WireMockInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private final static Logger logger = LoggerFactory.getLogger(WireMockInitializer.class);

    private final static WireMockServer wireMockServer = new WireMockServer(
        wireMockConfig().dynamicPort().extensions(new FunctionalResponseTransformer(false))
    );

    @Override
    public void initialize(@NotNull ConfigurableApplicationContext configurableApplicationContext) {
        if (!wireMockServer.isRunning()) {
            logger.info("Starting WireMock Server.");
            wireMockServer.start();
        }

        configurableApplicationContext.getBeanFactory().registerSingleton("wireMockServer", wireMockServer);
        System.setProperty("WIREMOCK_PORT", Integer.toString(wireMockServer.port()));

        configurableApplicationContext.addApplicationListener(applicationEvent -> {
            if (applicationEvent instanceof ContextClosedEvent) {
                logger.info("Stopping WireMock Server.");
                wireMockServer.stop();
            }
        });
    }

}
