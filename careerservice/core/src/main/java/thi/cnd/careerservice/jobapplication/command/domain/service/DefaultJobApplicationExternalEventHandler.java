package thi.cnd.careerservice.jobapplication.command.domain.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.jobapplication.command.port.JobApplicationEventStore;
import thi.cnd.careerservice.jobapplication.command.port.JobApplicationExternalEventHandler;
import thi.cnd.careerservice.jobapplication.query.port.JobApplicationQueryPort;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent;
import thi.cnd.careerservice.user.model.UserId;

@Service
@RequiredArgsConstructor
public class DefaultJobApplicationExternalEventHandler implements JobApplicationExternalEventHandler {

    private final JobApplicationEventStore eventStore;
    private final JobApplicationQueryPort jobApplicationQueryPort;

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

    @Override
    public void deleteJobApplication(UserId userId) {
        jobApplicationQueryPort.getAllJobApplicationsForUserId(userId).forEach(jobApplication ->
            CompletableFuture.runAsync(() ->
                eventStore.forcePublishEvents(jobApplication.getId(),
                    List.of(new JobApplicationEvent.JobApplicationDeleted(jobApplication.getId())))
            )
        );
    }

}
