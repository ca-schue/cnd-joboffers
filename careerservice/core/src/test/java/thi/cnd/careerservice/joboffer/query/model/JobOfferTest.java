package thi.cnd.careerservice.joboffer.query.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import thi.cnd.careerservice.joboffer.command.domain.model.JobOffer;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferStatus;
import thi.cnd.careerservice.joboffer.command.domain.model.SalaryRange;
import thi.cnd.careerservice.shared.event.model.JobOfferEvent;
import thi.cnd.careerservice.exception.IdentifiedRuntimeException;
import thi.cnd.careerservice.exception.ResourceDisallowsModificationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class JobOfferTest {

    private static final List<String> METHODS_THAT_REQUIRE_MODIFICATION_VALIDATION = List.of("setStatus", "setTitle", "setAttributes");
    private static final int AMOUNT_OF_METHODS_THAT_REQUIRE_NO_MODIFICATION_VALIDATION = 24;
    private static final List<String> METHODS_TO_IGNORE_FOR_MODIFICATION_VALIDATION_TEST = List.of("equals", "hashCode", "allowsModification", "validateModification");

    @Test
    void methodsThatRequireModificationValidationCheckIfModificationIsAllowed() {
        var methodsToTest = JobOffer.class.getMethods();
        var methodsThatDidNotRequireModificationValidation = 0;
        for (Method method : methodsToTest) {

            if (METHODS_TO_IGNORE_FOR_MODIFICATION_VALIDATION_TEST.contains(method.getName())) {
                continue;
            }

            var jobOffer = mock(JobOffer.class, Mockito.CALLS_REAL_METHODS);
            final Object[] methodArgs = Arrays.stream(method.getParameters()).map(par -> Mockito.any(par.getType())).toArray();

            try {
                method.invoke(jobOffer, methodArgs);
            } catch (NullPointerException | IllegalAccessException | InvocationTargetException ignored) {
                // Expected exceptions due to provided random arguments
            }

            var shouldCallModificationValidation = METHODS_THAT_REQUIRE_MODIFICATION_VALIDATION.contains(method.getName());

            if (!shouldCallModificationValidation) {
                methodsThatDidNotRequireModificationValidation++;
            }

            verify(jobOffer, times(shouldCallModificationValidation ? 1 : 0)).validateModification();
        }

        assertEquals(
            AMOUNT_OF_METHODS_THAT_REQUIRE_NO_MODIFICATION_VALIDATION, methodsThatDidNotRequireModificationValidation,
            "Methods that require no modification validation do not match the configured amount: actual=" + methodsThatDidNotRequireModificationValidation
        );
    }

    @Test
    void throwExceptionIfIllegalModificationIsCalledForClosedJobOffer() {
        var jobOffer = JobOfferTestProvider.createJobOffer(JobOfferStatus.CLOSED);
        assertThrows(ResourceDisallowsModificationException.class, () -> jobOffer.setTitle("ANY"));
    }

    @Test
    void throwExceptionIfIllegalModificationIsCalledForDeletedJobOffer() {
        var jobOffer = JobOfferTestProvider.createJobOffer(JobOfferStatus.DELETED);
        assertThrows(ResourceDisallowsModificationException.class, () -> jobOffer.setTitle("ANY"));
    }

    @Test
    void createJobOfferShouldResultInOneUncommittedCreationEvent() {
        var jobOffer = JobOffer.init().create(null, null, null, null, null, null, null);

        Assertions.assertEquals(1, jobOffer.getUncommittedEvents().size());
        assertInstanceOf(JobOfferEvent.JobOfferCreated.class, jobOffer.getUncommittedEvents().peek());
    }

    @Test
    void createJobOfferShouldSetAllValues() {
        var jobOffer = JobOfferTestProvider.createOpenJobOffer();
        Assertions.assertEquals(JobOfferTestProvider.COMPANY_ID, jobOffer.getCompanyId());
        Assertions.assertEquals(JobOfferTestProvider.CREATED_BY, jobOffer.getCreatedBy());
        Assertions.assertEquals(JobOfferTestProvider.TITLE, jobOffer.getTitle());
        Assertions.assertEquals(JobOfferTestProvider.DESCRIPTION, jobOffer.getDescription());
        Assertions.assertEquals(JobOfferStatus.OPEN, jobOffer.getStatus());
        Assertions.assertEquals(JobOfferTestProvider.TAGS, jobOffer.getTags());
        Assertions.assertEquals(JobOfferTestProvider.SALARY_RANGE, jobOffer.getSalaryRange());
    }

    @Test
    void statusCanBeUpdatedAndPublishesCorrectEvent() {
        var jobOffer = JobOfferTestProvider.createJobOffer(JobOfferStatus.DRAFT);
        jobOffer.getUncommittedEvents().clear();

        jobOffer.setStatus(JobOfferStatus.OPEN);

        Assertions.assertEquals(1, jobOffer.getUncommittedEvents().size());
        assertInstanceOf(JobOfferEvent.JobOfferStatusUpdated.class, jobOffer.getUncommittedEvents().peek());
    }

    @Test
    void statusCannotBeUpdatedBackToDraft() {
        var jobOffer = JobOfferTestProvider.createOpenJobOffer();
        var exception =assertThrows(IdentifiedRuntimeException.class, () -> jobOffer.setStatus(JobOfferStatus.DRAFT));
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    }

    @Test
    void statusCannotBeUpdatedToDeleted() {
        var jobOffer1 = JobOfferTestProvider.createOpenJobOffer();
        assertThrows(IllegalArgumentException.class, () -> jobOffer1.setStatus(JobOfferStatus.DELETED));
    }

    @Test
    void statusUpdateDeniedForInvalidChanges() {
        var jobOffer1 = JobOfferTestProvider.createJobOffer(JobOfferStatus.CLOSED);
        assertThrows(ResourceDisallowsModificationException.class, () -> jobOffer1.setStatus(JobOfferStatus.OPEN));

        var jobOffer2 = JobOfferTestProvider.createJobOffer(JobOfferStatus.DELETED);
        assertThrows(ResourceDisallowsModificationException.class, () -> jobOffer2.setStatus(JobOfferStatus.CLOSED));
    }

    @Test
    void newStatusAndCurrentStatusIsEqualResultsInNotModifiedException() {
        var jobOffer = JobOfferTestProvider.createOpenJobOffer();
        var exception = assertThrows(IdentifiedRuntimeException.class, () -> jobOffer.setStatus(JobOfferStatus.OPEN));
        assertEquals(HttpStatus.NOT_MODIFIED, exception.getStatus());
    }

    @Test
    void titleCanBeUpdatedAndPublishesCorrectEvent() {
        var jobOffer = JobOfferTestProvider.createOpenJobOffer();
        var title = "ANY";
        jobOffer.getUncommittedEvents().clear();

        jobOffer.setTitle(title);

        Assertions.assertEquals(1, jobOffer.getUncommittedEvents().size());
        assertInstanceOf(JobOfferEvent.JobOfferTitleChanged.class, jobOffer.getUncommittedEvents().peek());
    }

    @Test
    void settingTitleDoesNotPublishEventIfCurrentTitleIsTheSame() {
        var jobOffer = JobOfferTestProvider.createOpenJobOffer();
        jobOffer.getUncommittedEvents().clear();
        jobOffer.setTitle(new String(jobOffer.getTitle()));
        Assertions.assertEquals(0, jobOffer.getUncommittedEvents().size());
    }

    @Test
    void attributesCanBeSetAndPublishesEventAccordingly() {
        var jobOffer = JobOfferTestProvider.createOpenJobOffer();
        var newDescription = "NewDescription";
        var newTags = List.of("Tag1", "Tag2", "Tag3");
        var newSalaryRange = new SalaryRange(Money.of(1, "EUR"), Money.of(10, "EUR"));
        jobOffer.getUncommittedEvents().clear();

        jobOffer.setAttributes(newDescription, newTags, newSalaryRange);

        assertEquals(newDescription, jobOffer.getDescription());
        assertEquals(newTags, jobOffer.getTags());
        assertEquals(newDescription, jobOffer.getDescription());
        assertEquals(newSalaryRange, jobOffer.getSalaryRange());

        Assertions.assertEquals(1, jobOffer.getUncommittedEvents().size());
        assertInstanceOf(JobOfferEvent.JobOfferAttributesChanged.class, jobOffer.getUncommittedEvents().peek());
    }

    @Test
    void jobOfferCanBeDeletedAndPublishesCorrectEvent() {
        var jobOffer = JobOfferTestProvider.createOpenJobOffer();
        jobOffer.getUncommittedEvents().clear();

        jobOffer.delete();

        assertTrue(jobOffer.isDeleted());
        Assertions.assertEquals(1, jobOffer.getUncommittedEvents().size());
        assertInstanceOf(JobOfferEvent.JobOfferDeleted.class, jobOffer.getUncommittedEvents().peek());
    }

}
