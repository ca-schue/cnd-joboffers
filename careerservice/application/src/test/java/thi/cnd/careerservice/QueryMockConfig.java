package thi.cnd.careerservice;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationStatus;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationTestProvider;
import thi.cnd.careerservice.jobapplication.query.domain.model.JobApplicationView;
import thi.cnd.careerservice.jobapplication.query.port.JobApplicationQueryPort;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferId;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferStatus;
import thi.cnd.careerservice.joboffer.query.domain.model.JobOfferView;
import thi.cnd.careerservice.joboffer.query.port.JobOfferQueryPort;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent.JobApplicationCreated;
import thi.cnd.careerservice.shared.event.model.JobOfferEvent;
import thi.cnd.careerservice.shared.event.model.RecordedDomainEventWithMetadata;
import thi.cnd.careerservice.shared.event.model.RecordedEventMetadata;
import thi.cnd.careerservice.user.model.UserId;
import thi.cnd.careerservice.setup.mock.query.QueryPortTestConfig;

import static thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationStatus.OPEN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Component
@Profile("integrationtest")
public class QueryMockConfig {

    @Autowired
    protected JobOfferQueryPort jobOfferQueryPort;

    @Autowired
    protected JobApplicationQueryPort jobApplicationQueryPort;

    @Value("${" + QueryPortTestConfig.MOCK_JOB_OFFER_QUERY_PORT_PROPERTY + ":false}")
    private boolean mockJobOfferQueryPort;

    @Value("${" + QueryPortTestConfig.MOCK_JOB_APPLICATION_QUERY_PORT_PROPERTY + ":false}")
    private boolean mockJobApplicationQueryPort;

    public void reset() {
        if (!mockJobOfferQueryPort && !mockJobApplicationQueryPort) {
            mockJobOfferResponse(new CompanyId());
            mockGetAllJobApplicationsForJobOfferWithStatus(3);
        }
    }

    public void mockJobOfferResponse(CompanyId companyId) {
        if (mockJobOfferQueryPort) {
            when(jobOfferQueryPort.getJobOffer(any())).thenAnswer(context -> {
                var jobOfferId = (JobOfferId) context.getArgument(0);
                return provideJobOfferView(jobOfferId, companyId);
            });
        }
    }

    public void mockGetAllJobApplicationsForJobOfferWithStatus(int amountToReturn) {
        if (mockJobApplicationQueryPort) {
            when(jobApplicationQueryPort.getAllJobApplicationsForJobOfferWithStatus(any(), any())).thenAnswer(context -> {
                var jobOfferId = (JobOfferId) context.getArgument(0);
                List<JobApplicationView> answer = new ArrayList<>();
                for(int i=0; i < amountToReturn; i++) {
                    answer.add(provideJobApplicationView(jobOfferId));
                }
                return answer;
            });
        }
    }

    public void mockGetAllJobApplicationsForJobOfferWithStatus(List<JobApplicationView> jobApplicationViews) {
        if (mockJobApplicationQueryPort) {
            when(jobApplicationQueryPort.getAllJobApplicationsForJobOfferWithStatus(any(), any()))
                .thenReturn(jobApplicationViews);
        }
    }

    @NotNull
    public static JobOfferView provideJobOfferView(JobOfferId jobOfferId, CompanyId companyId) {
        return new JobOfferView(new RecordedDomainEventWithMetadata<>(
            new JobOfferEvent.JobOfferCreated(
                jobOfferId,
                companyId,
                new UserId(),
                "",
                "",
                JobOfferStatus.OPEN,
                List.of(),
                null
            ),
            new RecordedEventMetadata("", 0, 0, ""))
        );
    }

    @NotNull
    public static JobApplicationView provideJobApplicationView(JobOfferId jobOfferId) {
        return provideJobApplicationView(jobOfferId, OPEN);
    }

    @NotNull
    public static JobApplicationView provideJobApplicationView(JobOfferId jobOfferId, JobApplicationStatus status) {
        return new JobApplicationView(new RecordedDomainEventWithMetadata<>(
            (JobApplicationCreated) JobApplicationTestProvider.createJobApplication(status, jobOfferId).getUncommittedEvents().poll(),
            new RecordedEventMetadata("", 0, 0, ""))
        );
    }



}
