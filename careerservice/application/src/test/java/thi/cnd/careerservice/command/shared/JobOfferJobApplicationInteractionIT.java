package thi.cnd.careerservice.command.shared;

import java.util.List;

import org.junit.jupiter.api.Test;

import thi.cnd.careerservice.AbstractIntegrationTest;
import thi.cnd.careerservice.MockQueryPorts;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferStatus;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferTestProvider;
import thi.cnd.careerservice.QueryMockConfig;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent.JobApplicationDeleted;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent.JobApplicationStatusChanged;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent.JobApplicationViewOnlyEvent.JobApplicationJobOfferUpdated;

import static thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationStatus.ACCEPTED;
import static thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationStatus.DELETED;
import static thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationStatus.DENIED;
import static thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationStatus.DRAFT;
import static thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationStatus.OPEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MockQueryPorts
class JobOfferJobApplicationInteractionIT extends AbstractIntegrationTest {

    @Test
    void deletingAJobOfferAlsoDeletesAllAssociatedJobApplications() {
        var jobOffer = JobOfferTestProvider.createOpenJobOffer();
        jobOffer.delete();

        queryMockConfig.mockGetAllJobApplicationsForJobOfferWithStatus(2);

        jobOfferEventStore.forcePublishEvents(
            jobOffer.getId(),
            jobOffer.getUncommittedEvents().stream().toList()
        );

        testEventStoreListener.deleteTillMatch(event -> event instanceof JobApplicationDeleted);
        assertNotNull(testEventStoreListener.awaitNextSpecificEvent(JobApplicationDeleted.class));
        assertNotNull(testEventStoreListener.awaitNextSpecificEvent(JobApplicationDeleted.class));
        assertTrue(testEventStoreListener.isEmpty());
    }

    @Test
    void updatingJobOfferTitleAlsoUpdatesJobApplicationJobOfferTitle() {
        String newTitle = "NEW TITLE!";
        var jobOffer = JobOfferTestProvider.createOpenJobOffer();
        jobOffer.setTitle(newTitle);

        queryMockConfig.mockGetAllJobApplicationsForJobOfferWithStatus(2);

        jobOfferEventStore.forcePublishEvents(
            jobOffer.getId(),
            jobOffer.getUncommittedEvents().stream().toList()
        );

        testEventStoreListener.deleteTillMatch(event -> event instanceof JobApplicationJobOfferUpdated);
        assertEquals(newTitle, testEventStoreListener.awaitNextSpecificEvent(JobApplicationJobOfferUpdated.class).title());
        assertEquals(newTitle, testEventStoreListener.awaitNextSpecificEvent(JobApplicationJobOfferUpdated.class).title());
        assertTrue(testEventStoreListener.isEmpty());
    }

    @Test
    void closingJobOfferTitleDeniesAllOpenJobApplicationsAndDeletesAllDrafts() {
        var jobOffer = JobOfferTestProvider.createOpenJobOffer();
        jobOffer.setStatus(JobOfferStatus.CLOSED);

        var draft = QueryMockConfig.provideJobApplicationView(jobOffer.getId(), DRAFT);
        queryMockConfig.mockGetAllJobApplicationsForJobOfferWithStatus(
            List.of(
                QueryMockConfig.provideJobApplicationView(jobOffer.getId(), OPEN),
                QueryMockConfig.provideJobApplicationView(jobOffer.getId(), OPEN),
                draft,
                QueryMockConfig.provideJobApplicationView(jobOffer.getId(), DENIED),
                QueryMockConfig.provideJobApplicationView(jobOffer.getId(), ACCEPTED),
                QueryMockConfig.provideJobApplicationView(jobOffer.getId(), DELETED)
            )
        );

        jobOfferEventStore.forcePublishEvents(
            jobOffer.getId(),
            jobOffer.getUncommittedEvents().stream().toList()
        );

        testEventStoreListener.deleteTillMatch(event -> event instanceof JobApplicationStatusChanged || event instanceof JobApplicationDeleted);
        assertEquals(DENIED, testEventStoreListener.deleteFirst(JobApplicationStatusChanged.class).status());
        assertEquals(DENIED, testEventStoreListener.deleteFirst(JobApplicationStatusChanged.class).status());
        assertEquals(draft.getId(), testEventStoreListener.deleteFirst(JobApplicationDeleted.class).id());
        assertTrue(testEventStoreListener.isEmpty());
    }

}
