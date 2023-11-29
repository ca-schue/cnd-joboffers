package thi.cnd.careerservice.joboffer.command.port;

import thi.cnd.careerservice.joboffer.command.domain.model.JobOffer;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferId;
import thi.cnd.careerservice.shared.event.EventStore;
import thi.cnd.careerservice.shared.event.model.JobOfferEvent;

public interface JobOfferEventStore extends EventStore<JobOfferId, JobOffer, JobOfferEvent> {}
