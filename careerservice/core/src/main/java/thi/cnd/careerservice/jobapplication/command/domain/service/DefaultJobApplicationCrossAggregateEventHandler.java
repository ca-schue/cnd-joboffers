package thi.cnd.careerservice.jobapplication.command.domain.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import thi.cnd.careerservice.jobapplication.command.port.JobApplicationCrossAggregateEventHandler;
import thi.cnd.careerservice.jobapplication.command.port.JobApplicationEventStore;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationStatus;
import thi.cnd.careerservice.jobapplication.query.port.JobApplicationQueryPort;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferId;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferStatus;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent;

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

}
