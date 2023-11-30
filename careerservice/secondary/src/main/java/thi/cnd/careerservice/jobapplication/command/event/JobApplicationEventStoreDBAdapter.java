package thi.cnd.careerservice.jobapplication.command.event;

import org.springframework.stereotype.Component;

import com.eventstore.dbclient.EventStoreDBClient;

import thi.cnd.careerservice.jobapplication.command.port.JobApplicationEventStore;
import thi.cnd.careerservice.shared.model.BaseUUID;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplication;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationId;
import thi.cnd.careerservice.shared.event.EventStoreDBAdapter;
import thi.cnd.careerservice.shared.event.config.EventStoreSerializer;

@Component
public class JobApplicationEventStoreDBAdapter extends EventStoreDBAdapter<JobApplicationId, JobApplication, JobApplicationEvent> implements
    JobApplicationEventStore {

    public JobApplicationEventStoreDBAdapter(EventStoreDBClient eventStoreClient, EventStoreSerializer eventStoreSerializer) {
        super(eventStoreClient, eventStoreSerializer, BaseUUID::toString, JobApplication::init);
    }

}
