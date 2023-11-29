package thi.cnd.careerservice.joboffer.command.event;

import org.springframework.stereotype.Component;

import com.eventstore.dbclient.EventStoreDBClient;

import thi.cnd.careerservice.joboffer.command.port.JobOfferEventStore;
import thi.cnd.careerservice.shared.model.BaseUUID;
import thi.cnd.careerservice.shared.event.model.JobOfferEvent;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOffer;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferId;
import thi.cnd.careerservice.shared.event.EventStoreDBAdapter;
import thi.cnd.careerservice.shared.event.config.EventStoreSerializer;

@Component
public class JobOfferEventStoreDBAdapter extends EventStoreDBAdapter<JobOfferId, JobOffer, JobOfferEvent> implements JobOfferEventStore {

    public JobOfferEventStoreDBAdapter(EventStoreDBClient eventStoreClient, EventStoreSerializer eventStoreSerializer) {
        super(eventStoreClient, eventStoreSerializer, BaseUUID::toString, JobOffer::init);
    }

}
