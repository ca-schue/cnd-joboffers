package thi.cnd.careerservice.setup.mock.query;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import thi.cnd.careerservice.jobapplication.query.port.JobApplicationQueryPort;
import thi.cnd.careerservice.joboffer.query.port.JobOfferQueryPort;

import static org.mockito.Mockito.mock;

@Configuration
public class QueryPortTestConfig {

    public static final String MOCK_JOB_OFFER_QUERY_PORT_PROPERTY = "test.mockJobOfferQueryPort";
    public static final String MOCK_JOB_APPLICATION_QUERY_PORT_PROPERTY = "test.mockJobApplicationQueryPort";

    @Bean
    @Primary
    @ConditionalOnProperty(value = MOCK_JOB_OFFER_QUERY_PORT_PROPERTY, havingValue = "true")
    JobOfferQueryPort jobOfferQueryPort() {
        return mock(JobOfferQueryPort.class);
    }

    @Bean
    @Primary
    @ConditionalOnProperty(value = MOCK_JOB_APPLICATION_QUERY_PORT_PROPERTY, havingValue = "true")
    JobApplicationQueryPort jobApplicationQueryPort() {
        return mock(JobApplicationQueryPort.class);
    }

}
