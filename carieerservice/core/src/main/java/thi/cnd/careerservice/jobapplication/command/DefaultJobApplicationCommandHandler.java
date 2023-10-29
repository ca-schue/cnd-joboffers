package thi.cnd.careerservice.jobapplication.command;

import org.springframework.stereotype.Component;

import thi.cnd.careerservice.jobapplication.event.JobApplicationEventStore;
import thi.cnd.careerservice.shared.model.DataWithETag;
import thi.cnd.careerservice.shared.model.ETag;
import thi.cnd.careerservice.jobapplication.command.JobApplicationCommand.CreateJobApplication;
import thi.cnd.careerservice.jobapplication.command.JobApplicationCommand.DeleteJobApplication;
import thi.cnd.careerservice.jobapplication.command.JobApplicationCommand.UpdateJobApplicationStatus;
import thi.cnd.careerservice.jobapplication.domain.JobApplicationDomainService;
import thi.cnd.careerservice.jobapplication.model.JobApplication;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DefaultJobApplicationCommandHandler implements JobApplicationCommandHandler {

    private final JobApplicationDomainService domainService;
    private final JobApplicationEventStore eventStore;

    @Override
    public DataWithETag<JobApplication> createJobApplication(CreateJobApplication command) {
        var newJobApplication = domainService.createJobApplication(command);
        return eventStore.publishFirstEventAndCreateNewEventStream(newJobApplication);
    }

    @Override
    public DataWithETag<JobApplication> deleteJobApplication(DeleteJobApplication command, ETag expectedRevision) {
        return eventStore.updateAndPublish(command.id(), expectedRevision, JobApplication::delete);
    }

    @Override
    public DataWithETag<JobApplication> updateStatus(UpdateJobApplicationStatus command, ETag expectedRevision) {
        return eventStore.updateAndPublish(command.id(), expectedRevision,
            jobApplication -> jobApplication.setStatus(command.status())
        );
    }

    @Override
    public DataWithETag<JobApplication> updateStatus(JobApplicationCommand.UpdateJobApplicationAttributes command, ETag expectedRevision) {
        return eventStore.updateAndPublish(command.id(), expectedRevision,
            jobApplication -> jobApplication.setContent(command.content())
        );
    }

}
