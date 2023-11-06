package thi.cnd.careerservice.command.jobapplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import thi.cnd.careerservice.jobapplication.event.JobApplicationEventStore;
import thi.cnd.careerservice.jobapplication.model.JobApplication;
import thi.cnd.careerservice.jobapplication.model.JobApplicationId;
import thi.cnd.careerservice.EndpointMockConfig;
import thi.cnd.careerservice.QueryMockConfig;
import thi.cnd.careerservice.shared.DataWithVersion;
import thi.cnd.careerservice.test.api.ApiClient;
import thi.cnd.careerservice.test.api.generated.JobApplicationCommandApi;
import thi.cnd.careerservice.test.api.generated.model.JobApplicationCreationRequestDTO;
import thi.cnd.careerservice.user.model.UserId;

@Component
public class JobApplicationCommandApiTestAdapter {

    private static final Logger logger = LoggerFactory.getLogger(JobApplicationCommandApiTestAdapter.class);

    private final JobApplicationCommandApi commandApi;
    private final JobApplicationEventStore jobApplicationEventStore;
    private final QueryMockConfig queryMockConfig;
    private final EndpointMockConfig endpointMockConfig;

    public JobApplicationCommandApiTestAdapter(@Value("${server.port}") int port, WebClient webClient,
        JobApplicationEventStore jobOfferEventStore, QueryMockConfig queryMockConfig, EndpointMockConfig endpointMockConfig) {
        this.commandApi = new JobApplicationCommandApi(new ApiClient(webClient).setBasePath("http://localhost:" + port));
        this.jobApplicationEventStore = jobOfferEventStore;
        this.queryMockConfig = queryMockConfig;
        this.endpointMockConfig = endpointMockConfig;
    }

    public DataWithVersion<JobApplication> create(JobApplicationFixture.Builder builder) {
        try {

            queryMockConfig.mockJobOfferResponse(builder.getCompanyId());
            endpointMockConfig.mockUserResponse(builder.getUserId());
            endpointMockConfig.mockCompanyResponse(builder.getCompanyId(), new UserId());

            var response = commandApi.createJobApplication(
                builder.getUserId().getId(),
                builder.getJobOfferId().getId(),
                new JobApplicationCreationRequestDTO()
                    .companyId(builder.getCompanyId().getId())
                    .status(builder.getStatus().toString())
                    .content(builder.getContent())
            ).block();

            return DataWithVersion.of(jobApplicationEventStore.getWithVersion(new JobApplicationId(response.getId())));
        } catch (WebClientResponseException e) {
            logger.error("Could not call 'CreateJobOffer' endpoint with message {}", e.getResponseBodyAsString());
            throw e;
        }
    }

}
