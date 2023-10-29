package thi.cnd.careerservice.jobapplication.event;

import thi.cnd.careerservice.jobapplication.model.JobApplication;
import thi.cnd.careerservice.jobapplication.model.JobApplicationId;
import thi.cnd.careerservice.shared.event.EventStore;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent;

public interface JobApplicationEventStore extends EventStore<JobApplicationId, JobApplication, JobApplicationEvent> {}
