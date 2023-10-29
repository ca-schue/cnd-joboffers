package thi.cnd.careerservice.joboffer.domain;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.eventstore.dbclient.StreamNotFoundException;

import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.joboffer.view.model.JobOfferView;
import thi.cnd.careerservice.shared.event.EventStore;
import thi.cnd.careerservice.shared.event.model.JobOfferEvent;
import thi.cnd.careerservice.joboffer.model.JobOffer;
import thi.cnd.careerservice.joboffer.model.JobOfferId;
import thi.cnd.careerservice.joboffer.view.query.JobOfferQueryService;
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
