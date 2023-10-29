package thi.cnd.careerservice.joboffer.event;

import thi.cnd.careerservice.joboffer.model.JobOffer;
import thi.cnd.careerservice.joboffer.model.JobOfferId;
import thi.cnd.careerservice.shared.event.EventStore;
import thi.cnd.careerservice.shared.event.model.JobOfferEvent;

public interface JobOfferEventStore extends EventStore<JobOfferId, JobOffer, JobOfferEvent> {}
