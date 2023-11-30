package thi.cnd.careerservice.jobapplication.command.port;

import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplication;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationId;
import thi.cnd.careerservice.shared.event.EventStore;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent;

public interface JobApplicationEventStore extends EventStore<JobApplicationId, JobApplication, JobApplicationEvent> {}
