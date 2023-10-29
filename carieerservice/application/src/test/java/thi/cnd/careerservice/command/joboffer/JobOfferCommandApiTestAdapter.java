package thi.cnd.careerservice.command.joboffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import thi.cnd.careerservice.joboffer.event.JobOfferEventStore;
import thi.cnd.careerservice.joboffer.model.JobOffer;
import thi.cnd.careerservice.joboffer.model.JobOfferId;
import thi.cnd.careerservice.shared.ApiMapper;
import thi.cnd.careerservice.shared.DataWithVersion;
import thi.cnd.careerservice.test.api.ApiClient;
import thi.cnd.careerservice.test.api.generated.JobOfferCommandApi;
import thi.cnd.careerservice.test.api.generated.model.JobOfferCreationRequestDTO;
import thi.cnd.careerservice.test.api.generated.model.JobOfferUpdateRequestDTO;
import thi.cnd.careerservice.test.api.generated.model.SalaryRangeDTO;

@Component
public class JobOfferCommandApiTestAdapter {

    private static final Logger logger = LoggerFactory.getLogger(JobOfferCommandApiTestAdapter.class);

    private final JobOfferCommandApi commandApi;
    private final JobOfferEventStore jobOfferEventStore;

    public JobOfferCommandApiTestAdapter(@Value("${server.port}") int port, WebClient webClient,
        JobOfferEventStore jobOfferEventStore) {
        this.commandApi = new JobOfferCommandApi(new ApiClient(webClient).setBasePath("http://localhost:" + port));
        this.jobOfferEventStore = jobOfferEventStore;
    }

    public DataWithVersion<JobOffer> create(JobOfferFixture.Builder builder) {
        try {
            var response = commandApi.createJobOffer(
                builder.getCompanyId().getId(),
                new JobOfferCreationRequestDTO()
                    .createdBy(builder.getCreatedBy().getId())
                    .companyId(builder.getCompanyId().getId())
                    .title(builder.getTitle())
                    .description(builder.getDescription())
                    .status(builder.getStatus().toString())
                    .tags(builder.getTags())
                    .salaryRange(
                        new SalaryRangeDTO()
                            .lowerBound(ApiMapper.toMoneyDTO(builder.getSalaryRange().lowerBound()))
                            .upperBound(ApiMapper.toMoneyDTO(builder.getSalaryRange().upperBound()))
                    )
            ).block();

            return DataWithVersion.of(jobOfferEventStore.getWithVersion(new JobOfferId(response.getId())));
        } catch (WebClientResponseException e) {
            logger.error("Could not call 'CreateJobOffer' endpoint with message {}", e.getResponseBodyAsString());
            throw e;
        }
    }

    public DataWithVersion<JobOffer> publish(DataWithVersion<JobOffer> jobOfferWithVersion) {
        var jobOffer = jobOfferWithVersion.get();

        try {
            commandApi.publishJobOffer(jobOffer.getCompanyId().getId(), jobOffer.getId().getId(), jobOfferWithVersion.getVersionAsString())
                .block();
        } catch (WebClientResponseException e) {
            logger.error("Could not call 'PublishJobOffer' endpoint with message {}", e.getResponseBodyAsString());
            throw e;
        }

        return DataWithVersion.of(jobOfferEventStore.getWithVersion(jobOffer.getId()));
    }

    public DataWithVersion<JobOffer> updateTitle(DataWithVersion<JobOffer> jobOfferWithVersion, String title) {
        return update(jobOfferWithVersion, title, null);
    }

    public DataWithVersion<JobOffer> updateDescription(DataWithVersion<JobOffer> jobOfferWithVersion, String description) {
        return update(jobOfferWithVersion, null, description);
    }

    public DataWithVersion<JobOffer> update(DataWithVersion<JobOffer> jobOfferWithVersion, String title, String description) {
        var jobOffer = jobOfferWithVersion.get();

        try {
            commandApi.updateJobOfferAttributes(
                jobOffer.getCompanyId().getId(),
                jobOffer.getId().getId(),
                jobOfferWithVersion.getVersionAsString(),
                new JobOfferUpdateRequestDTO()
                    .title(title != null ? title : jobOffer.getTitle())
                    .description(description != null ? description : jobOffer.getDescription())
                    .tags(jobOffer.getTags())
                    .salaryRange(
                        jobOffer.getSalaryRange() != null ?
                            new SalaryRangeDTO()
                                .lowerBound(ApiMapper.toMoneyDTO(jobOffer.getSalaryRange().lowerBound()))
                                .upperBound(ApiMapper.toMoneyDTO(jobOffer.getSalaryRange().upperBound()))
                            : null
                    )
            ).block();
        } catch (WebClientResponseException e) {
            logger.error("Could not call 'UpdateJobOfferTitle' endpoint with message {}", e.getResponseBodyAsString());
            throw e;
        }

        return DataWithVersion.of(jobOfferEventStore.getWithVersion(jobOffer.getId()));
    }


    public DataWithVersion<JobOffer> delete(DataWithVersion<JobOffer> jobOfferWithVersion) {
        var jobOffer = jobOfferWithVersion.get();

        try {
            commandApi.deleteJobOffer(
                jobOffer.getCompanyId().getId(),
                jobOffer.getId().getId(),
                jobOfferWithVersion.getVersionAsString()
            ).block();
        } catch (WebClientResponseException e) {
            logger.error("Could not call 'UpdateJobOfferTitle' endpoint with message {}", e.getResponseBodyAsString());
            throw e;
        }

        return DataWithVersion.of(jobOfferEventStore.getWithVersion(jobOffer.getId()));
    }
}
