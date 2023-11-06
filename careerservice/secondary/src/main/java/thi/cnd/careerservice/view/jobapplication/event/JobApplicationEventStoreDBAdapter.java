package thi.cnd.careerservice.view.jobapplication.event;

import org.springframework.stereotype.Component;

import com.eventstore.dbclient.EventStoreDBClient;

import thi.cnd.careerservice.jobapplication.event.JobApplicationEventStore;
import thi.cnd.careerservice.shared.model.BaseUUID;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent;
import thi.cnd.careerservice.jobapplication.model.JobApplication;
import thi.cnd.careerservice.jobapplication.model.JobApplicationId;
import thi.cnd.careerservice.event.EventStoreDBAdapter;
import thi.cnd.careerservice.event.config.EventStoreSerializer;

@Component
public class JobApplicationEventStoreDBAdapter extends EventStoreDBAdapter<JobApplicationId, JobApplication, JobApplicationEvent> implements
    JobApplicationEventStore {

    public JobApplicationEventStoreDBAdapter(EventStoreDBClient eventStoreClient, EventStoreSerializer eventStoreSerializer) {
        super(eventStoreClient, eventStoreSerializer, BaseUUID::toString, JobApplication::init);
    }

}
