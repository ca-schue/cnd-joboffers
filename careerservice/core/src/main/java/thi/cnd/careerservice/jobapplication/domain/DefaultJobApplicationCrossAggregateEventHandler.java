package thi.cnd.careerservice.jobapplication.domain;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.jobapplication.event.JobApplicationEventStore;
import thi.cnd.careerservice.jobapplication.model.JobApplicationStatus;
import thi.cnd.careerservice.jobapplication.view.query.JobApplicationQueryPort;
import thi.cnd.careerservice.joboffer.model.JobOfferId;
import thi.cnd.careerservice.joboffer.model.JobOfferStatus;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent;
import thi.cnd.careerservice.user.model.UserId;

@Component
@RequiredArgsConstructor
public class DefaultJobApplicationCrossAggregateEventHandler implements JobApplicationCrossAggregateEventHandler {

    private final JobApplicationEventStore eventStore;
    private final JobApplicationQueryPort jobApplicationQueryPort;

    @Override
    public void deleteJobApplication(JobOfferId id) {
        jobApplicationQueryPort.getAllJobApplicationsForJobOfferWithStatus(id, JobApplicationStatus.allExceptDeleted())
            .forEach(jobApplication ->
                CompletableFuture.runAsync(() ->
                    eventStore.forcePublishEvents(jobApplication.getId(),
                        List.of(new JobApplicationEvent.JobApplicationDeleted(jobApplication.getId())))
                )
            );
    }

    @Override
    public void deleteJobApplication(UserId userId) {
        jobApplicationQueryPort.getAllJobApplicationsForUserId(userId).forEach(jobApplication ->
            CompletableFuture.runAsync(() ->
                eventStore.forcePublishEvents(jobApplication.getId(),
                    List.of(new JobApplicationEvent.JobApplicationDeleted(jobApplication.getId())))
            )
        );
    }

    @Override
    public void applyJobOfferStatusChanged(JobOfferId id, JobOfferStatus status) {
        if (status == JobOfferStatus.CLOSED) {
            denyAllJobApplications(id);
        }

    }

    private void denyAllJobApplications(JobOfferId id) {
        jobApplicationQueryPort.getAllJobApplicationsForJobOfferWithStatus(id, List.of(JobApplicationStatus.DRAFT, JobApplicationStatus.OPEN))
            .forEach(jobApplication ->
                CompletableFuture.runAsync(() -> {
                    switch (jobApplication.getStatus()) {
                        case JobApplicationStatus.DRAFT -> eventStore.forcePublishEvents(jobApplication.getId(),
                            new JobApplicationEvent.JobApplicationDeleted(jobApplication.getId()));
                        case JobApplicationStatus.OPEN -> eventStore.forcePublishEvents(jobApplication.getId(),
                            new JobApplicationEvent.JobApplicationStatusChanged(jobApplication.getId(), JobApplicationStatus.DENIED));
                        default -> throw new IllegalStateException("The status is not supported");
                    }
                })
            );
    }

    @Override
    public void updateJobOfferTitle(JobOfferId id, String title) {
        jobApplicationQueryPort.getAllJobApplicationsForJobOfferWithStatus(id, JobApplicationStatus.allExceptDeleted())
            .forEach(jobApplication ->
                CompletableFuture.runAsync(() ->
                    eventStore.forcePublishEvents(jobApplication.getId(),
                        (new JobApplicationEvent.JobApplicationViewOnlyEvent.JobApplicationJobOfferUpdated(jobApplication.getId(), title)))
                )
            );

    }

    @Override
    public void updateCompanyName(CompanyId id, String name) {
        if (StringUtils.isBlank(name)) {
            return;
        }

        jobApplicationQueryPort.getAllJobApplicationsForCompanyId(id).forEach(jobApplication ->
            CompletableFuture.runAsync(() ->
                eventStore.forcePublishEvents(jobApplication.getId(),
                    List.of(
                        new JobApplicationEvent.JobApplicationViewOnlyEvent.JobApplicationCompanyNameUpdated(jobApplication.getId(), name)))
            )
        );
    }

}
