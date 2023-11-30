package thi.cnd.careerservice.joboffer.command.domain.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.eventstore.dbclient.StreamNotFoundException;

import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOffer;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferId;
import thi.cnd.careerservice.joboffer.command.port.JobOfferExternalEventHandler;
import thi.cnd.careerservice.joboffer.query.domain.model.JobOfferView;
import thi.cnd.careerservice.shared.event.EventStore;
import thi.cnd.careerservice.shared.event.model.JobOfferEvent;
import thi.cnd.careerservice.joboffer.query.application.service.JobOfferQueryService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DefaultJobOfferExternalEventHandler implements JobOfferExternalEventHandler {

    private final JobOfferQueryService jobOfferQueryService;
    private final EventStore<JobOfferId, JobOffer, JobOfferEvent> eventStore;

    private static final Logger logger = LoggerFactory.getLogger(DefaultJobOfferExternalEventHandler.class);

    @Override
    public void companyDeleted(CompanyId companyId) {
        jobOfferQueryService.getAllJobOffersByCompanyId(companyId).forEach(this::deleteJobOffer);
    }

    private void deleteJobOffer(JobOfferView jobOffer) {
        try {
            logger.info("Deleting Job Offer {} due to company {} being deleted", jobOffer.getId(), jobOffer.getCompanyId());
            eventStore.forcePublishEvents(jobOffer.getId(), List.of(new JobOfferEvent.JobOfferDeleted(jobOffer.getId())));
        } catch (StreamNotFoundException e) {
            // Ignore
        }
    }

}
