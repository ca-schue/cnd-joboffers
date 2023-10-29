package thi.cnd.careerservice.command.joboffer;

import org.junit.jupiter.api.Test;

import thi.cnd.careerservice.exception.ResourceDisallowsModificationException;
import thi.cnd.careerservice.joboffer.model.JobOfferStatus;
import thi.cnd.careerservice.AbstractIntegrationTest;
import thi.cnd.careerservice.shared.event.model.JobOfferEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BasicJobOfferCommandIT extends AbstractIntegrationTest {

    @Test
    void basicUsageTest() {
        var newTitle = "New Title";
        var newDescription = "New Description";

        var jobOfferWithVersion = jobOfferFixtureBuilder.createDraft().build();
        jobOfferWithVersion = jobOfferCommandTestAdapter.publish(jobOfferWithVersion);
        jobOfferWithVersion = jobOfferCommandTestAdapter.updateTitle(jobOfferWithVersion, newTitle);
        jobOfferWithVersion = jobOfferCommandTestAdapter.updateDescription(jobOfferWithVersion, newDescription);

        assertEquals(JobOfferStatus.OPEN, jobOfferWithVersion.get().getStatus());
        assertEquals(newTitle, jobOfferWithVersion.get().getTitle());
        assertEquals(newDescription, jobOfferWithVersion.get().getDescription());

        var events = getAllEvents(jobOfferWithVersion.get().getId());
        assertEquals(4, events.size());

        jobOfferWithVersion = jobOfferCommandTestAdapter.delete(jobOfferWithVersion);

        final var jobOffer = jobOfferWithVersion.get();
        assertTrue(jobOffer.isDeleted());
        assertThrows(ResourceDisallowsModificationException.class, () -> jobOffer.setTitle("ANY"));

        var deletedEvent = (JobOfferEvent.JobOfferDeleted) testEventStoreListener.peakFirst(event -> event instanceof JobOfferEvent.JobOfferDeleted jobOfferDeleted && jobOfferDeleted.id().equals(jobOffer.getId()));
        assertEquals(jobOffer.getId(), deletedEvent.id());
    }


}
