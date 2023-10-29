package thi.cnd.careerservice;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import thi.cnd.careerservice.command.jobapplication.JobApplicationCommandApiTestAdapter;
import thi.cnd.careerservice.command.jobapplication.JobApplicationFixture;
import thi.cnd.careerservice.command.joboffer.JobOfferCommandApiTestAdapter;
import thi.cnd.careerservice.command.joboffer.JobOfferFixture;
import thi.cnd.careerservice.jobapplication.event.JobApplicationEventStore;
import thi.cnd.careerservice.jobapplication.model.JobApplicationId;
import thi.cnd.careerservice.joboffer.event.JobOfferEventStore;
import thi.cnd.careerservice.joboffer.model.JobOfferId;
import thi.cnd.careerservice.setup.container.TestContainerManager;
import thi.cnd.careerservice.setup.mock.endpoint.WireMockInitializer;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent;
import thi.cnd.careerservice.shared.event.model.JobOfferEvent;

@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = {"integrationtest"})
@SpringBootTest(
    classes = Application.class,
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class, OAuth2ClientAutoConfiguration.class})
@ContextConfiguration(initializers = {WireMockInitializer.class})
@DirtiesContext
public abstract class AbstractIntegrationTest {

    @Autowired
    protected ConfigurableEnvironment environment;

    @Autowired
    protected EndpointMockConfig endpointMockConfig;

    @Autowired
    protected MongoTemplate mongoTemplate;

    @Autowired
    protected JobOfferFixture jobOfferFixtureBuilder;

    @Autowired
    protected JobOfferEventStore jobOfferEventStore;

    @Autowired
    protected JobOfferCommandApiTestAdapter jobOfferCommandTestAdapter;

    @Autowired
    protected JobApplicationFixture jobApplicationFixture;

    @Autowired
    protected JobApplicationEventStore jobApplicationEventStore;

    @Autowired
    protected JobApplicationCommandApiTestAdapter jobApplicationCommandTestAdapter;

    @Autowired
    protected TestEventStoreListener testEventStoreListener;

    @Autowired
    protected QueryMockConfig queryMockConfig;

    @Autowired
    protected RetryTemplate retry;

    public AbstractIntegrationTest() {
        TestContainerManager.startAll();
    }

    @BeforeEach
    void initTest() {
        endpointMockConfig.resetMocksToDefault();
        clearRepositoryStorage();
        testEventStoreListener.clearEventQueue();
        queryMockConfig.reset();
    }

    @AfterEach
    void tearDown() {
        endpointMockConfig.resetMocksToDefault();
        clearRepositoryStorage();
        testEventStoreListener.clearEventQueue();
        queryMockConfig.reset();
    }

    @DynamicPropertySource
    static void overrideApplicationProperties(DynamicPropertyRegistry registry) {
        TestContainerManager.overrideApplicationProperties(registry);
    }

    protected void clearRepositoryStorage() {
        mongoTemplate.getCollectionNames().forEach(mongoTemplate::dropCollection);
    }

    protected List<JobOfferEvent> getAllEvents(JobOfferId id) {
        return jobOfferEventStore.readAllEvents(id);
    }

    protected List<JobApplicationEvent> getAllEvents(JobApplicationId id) {
        return jobApplicationEventStore.readAllEvents(id);
    }

    protected <T> T poll(Supplier<T> supplier) {
        return retry.execute(context -> supplier.get());
    }

    protected void waitUntil(BooleanSupplier filter) {
        retry.execute(context -> {
            if (!filter.getAsBoolean()) {
                throw new NotYetProcessedException();
            }
            return null;
        });
    }

    protected <T> T waitUntil(Supplier<T> supplier, Predicate<T> isValid) {
        return retry.execute(context -> {
            var item = supplier.get();
            if (!isValid.test(item)) {
                throw new NotYetProcessedException();
            }
            return item;
        });
    }

}
