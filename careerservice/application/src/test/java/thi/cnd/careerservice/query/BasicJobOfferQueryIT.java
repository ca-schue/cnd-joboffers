package thi.cnd.careerservice.query;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import thi.cnd.careerservice.AbstractIntegrationTest;
import thi.cnd.careerservice.NotYetProcessedException;
import thi.cnd.careerservice.exception.ViewResourceNotFoundException;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOffer;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferStatus;
import thi.cnd.careerservice.joboffer.query.model.JobOfferTestProvider;
import thi.cnd.careerservice.joboffer.query.domain.model.JobOfferView;
import thi.cnd.careerservice.joboffer.query.JobOfferQueryAdapter;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BasicJobOfferQueryIT extends AbstractIntegrationTest {

    @Autowired
    private JobOfferQueryAdapter jobOfferQueryAdapter;

    @Test
    void basicTest1() {
        var jobOffer = JobOfferTestProvider.createJobOffer(JobOfferStatus.DRAFT);
        jobOfferEventStore.forcePublishEvents(jobOffer.getId(), jobOffer.getUncommittedEvents().stream().toList());
        jobOffer.getUncommittedEvents().clear();

        var view = poll(() -> jobOfferQueryAdapter.getJobOffer(jobOffer.getId()));

        validateJobOffer(jobOffer, view);

        jobOffer.setStatus(JobOfferStatus.OPEN);
        jobOffer.setTitle("NEW TITLE!");
        jobOffer.setAttributes("NEW DESC!", List.of("NEW TAGS"), null);
        jobOfferEventStore.forcePublishEvents(jobOffer.getId(), jobOffer.getUncommittedEvents().stream().toList());

        var updatedView = waitUntil(
            () -> jobOfferQueryAdapter.getJobOffer(jobOffer.getId()),
            newJobOfferView -> view.getMetadata().version() < newJobOfferView.getMetadata().version()
        );

        validateJobOffer(jobOffer, updatedView);
    }

    @Test
    void deletedJobOffersAreDeletedFromView() {
        var jobOffer = JobOfferTestProvider.createOpenJobOffer();
        jobOfferEventStore.forcePublishEvents(jobOffer.getId(), jobOffer.getUncommittedEvents().stream().toList());
        jobOffer.getUncommittedEvents().clear();

        poll(() -> jobOfferQueryAdapter.getJobOffer(jobOffer.getId()));

        jobOffer.delete();
        jobOfferEventStore.forcePublishEvents(jobOffer.getId(), jobOffer.getUncommittedEvents().stream().toList());
        jobOffer.getUncommittedEvents().clear();

        boolean wasDeleted = poll(() -> {
            try {
                jobOfferQueryAdapter.getJobOffer(jobOffer.getId());
                throw new NotYetProcessedException();
            } catch (ViewResourceNotFoundException e) {
                return true;
            }
        });

        assertTrue(wasDeleted);
    }

    private void validateJobOffer(JobOffer expected, JobOfferView actual) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getCompanyId(), actual.getCompanyId());
        Assertions.assertEquals(expected.getCreatedBy(), actual.getCreatedBy());
        Assertions.assertEquals(expected.getStatus(), actual.getStatus());
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getStatus(), actual.getStatus());
        Assertions.assertEquals(expected.getTags(), actual.getTags());
        Assertions.assertEquals(expected.getSalaryRange(), actual.getSalaryRange());
    }

}
