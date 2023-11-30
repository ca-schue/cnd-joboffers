package thi.cnd.careerservice.jobapplication.command.domain.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import thi.cnd.careerservice.exception.IdentifiedRuntimeException;
import thi.cnd.careerservice.exception.ResourceDisallowsModificationException;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent;

import static thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationStatus.DELETED;
import static thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationStatus.DRAFT;
import static thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationStatus.OPEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class JobApplicationTest {

    private static final List<String> METHODS_THAT_REQUIRE_MODIFICATION_VALIDATION = List.of("setStatus", "setContent");
    private static final int AMOUNT_OF_METHODS_THAT_REQUIRE_NO_MODIFICATION_VALIDATION = 22;
    private static final List<String> METHODS_TO_IGNORE_FOR_MODIFICATION_VALIDATION_TEST = List.of("equals", "hashCode", "allowsModification", "validateModification");

    /**
     * Validates that every method either checks if aggregate allows modification or explicitly does not want to check.
     */
    @Test
    void methodsThatRequireModificationValidationCheckIfModificationIsAllowed() {
        var methodsToTest = JobApplication.class.getMethods();
        var methodsThatDidNotRequireModificationValidation = 0;
        for (Method method : methodsToTest) {

            if (METHODS_TO_IGNORE_FOR_MODIFICATION_VALIDATION_TEST.contains(method.getName())) {
                continue;
            }

            var jobApplication = mock(JobApplication.class, Mockito.CALLS_REAL_METHODS);
            final Object[] methodArgs = Arrays.stream(method.getParameters()).map(par -> Mockito.any(par.getType())).toArray();

            try {
                method.invoke(jobApplication, methodArgs);
            } catch (NullPointerException | IllegalAccessException | InvocationTargetException ignored) {
                // Expected exceptions due to provided random arguments
            }

            var shouldCallModificationValidation = METHODS_THAT_REQUIRE_MODIFICATION_VALIDATION.contains(method.getName());

            if (!shouldCallModificationValidation) {
                methodsThatDidNotRequireModificationValidation++;
            }

            verify(jobApplication, times(shouldCallModificationValidation ? 1 : 0)).validateModification();
        }

        assertEquals(
            AMOUNT_OF_METHODS_THAT_REQUIRE_NO_MODIFICATION_VALIDATION, methodsThatDidNotRequireModificationValidation,
            "Methods that require no modification validation do not match the configured amount: actual=" + methodsThatDidNotRequireModificationValidation
        );
    }

    @Test
    void disallowsModificationIfAlreadyDeleted() {
        JobApplication jobApplication = JobApplicationTestProvider.createJobApplication(DRAFT);
        jobApplication.delete();

        assertThrows(ResourceDisallowsModificationException.class, () -> jobApplication.setContent("ANY"));
    }

    @Test
    void createJobApplicationSetsValuesCorrectlyAndFiresCorrectEvent() {
        JobApplication jobApplication = JobApplicationTestProvider.createOpenJobApplication();

        assertEquals(JobApplicationTestProvider.USER_ID, jobApplication.getUserId());
        assertEquals(JobApplicationTestProvider.COMPANY_ID, jobApplication.getCompanyId());
        assertEquals(JobApplicationTestProvider.JOB_OFFER_ID, jobApplication.getJobOfferId());
        assertEquals(OPEN, jobApplication.getStatus());
        assertEquals(JobApplicationTestProvider.CONTENT, jobApplication.getContent());

        assertEquals(1, jobApplication.getUncommittedEvents().size());
        assertInstanceOf(JobApplicationEvent.JobApplicationCreated.class, jobApplication.getUncommittedEvents().peek());
    }

    @Test
    void updateStatusFiresCorrectEvent() {
        JobApplication jobApplication = JobApplicationTestProvider.createJobApplication(DRAFT);
        jobApplication.getUncommittedEvents().clear();

        jobApplication.setStatus(OPEN);

        assertEquals(OPEN, jobApplication.getStatus());
        assertEquals(1, jobApplication.getUncommittedEvents().size());
        assertInstanceOf(JobApplicationEvent.JobApplicationStatusChanged.class, jobApplication.getUncommittedEvents().peek());
    }

    @Test
    void updateStatusThrowsExceptionIfAlreadyClosed() {
        JobApplication jobApplication = JobApplicationTestProvider.createJobApplication(DRAFT);
        jobApplication.setStatus(JobApplicationStatus.DENIED);

        assertThrows(ResourceDisallowsModificationException.class, () -> jobApplication.setStatus(OPEN));
    }

    @Test
    void cannotBePutBackIntoDraftAfterBeingPublished() {
        JobApplication jobApplication = JobApplicationTestProvider.createJobApplication(DRAFT);
        jobApplication.setStatus(OPEN);

        assertThrows(IdentifiedRuntimeException.class, () -> jobApplication.setStatus(DRAFT));
    }

    @Test
    void updateStatusThrowsExceptionIfAlreadyDeleted() {
        JobApplication jobApplication = JobApplicationTestProvider.createJobApplication(DRAFT);
        jobApplication.delete();

        assertThrows(IllegalArgumentException.class, () -> jobApplication.setStatus(OPEN));
    }

    @Test
    void updateContentFiresCorrectEvent() {
        JobApplication jobApplication = JobApplicationTestProvider.createOpenJobApplication();
        jobApplication.getUncommittedEvents().clear();

        jobApplication.setContent("ANY");

        assertEquals("ANY", jobApplication.getContent());
        assertEquals(1, jobApplication.getUncommittedEvents().size());
        assertInstanceOf(JobApplicationEvent.JobApplicationContentChanged.class, jobApplication.getUncommittedEvents().peek());
    }

    @Test
    void deleteFiresCorrectEvent() {
        JobApplication jobApplication = JobApplicationTestProvider.createOpenJobApplication();
        jobApplication.getUncommittedEvents().clear();

        jobApplication.delete();

        assertEquals(DELETED, jobApplication.getStatus());
        assertTrue(jobApplication.isDeleted());
        assertEquals(1, jobApplication.getUncommittedEvents().size());
        assertInstanceOf(JobApplicationEvent.JobApplicationDeleted.class, jobApplication.getUncommittedEvents().peek());
    }

}
