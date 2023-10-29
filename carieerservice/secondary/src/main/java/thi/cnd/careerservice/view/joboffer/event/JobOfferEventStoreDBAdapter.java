package thi.cnd.careerservice.view.joboffer.event;

import org.springframework.stereotype.Component;

import com.eventstore.dbclient.EventStoreDBClient;

import thi.cnd.careerservice.joboffer.event.JobOfferEventStore;
import thi.cnd.careerservice.shared.model.BaseUUID;
import thi.cnd.careerservice.shared.event.model.JobOfferEvent;
import thi.cnd.careerservice.joboffer.model.JobOffer;
import thi.cnd.careerservice.joboffer.model.JobOfferId;
import thi.cnd.careerservice.event.EventStoreDBAdapter;
import thi.cnd.careerservice.event.config.EventStoreSerializer;

@Component
public class JobOfferEventStoreDBAdapter extends EventStoreDBAdapter<JobOfferId, JobOffer, JobOfferEvent> implements JobOfferEventStore {

    public JobOfferEventStoreDBAdapter(EventStoreDBClient eventStoreClient, EventStoreSerializer eventStoreSerializer) {
        super(eventStoreClient, eventStoreSerializer, BaseUUID::toString, JobOffer::init);
    }

}
