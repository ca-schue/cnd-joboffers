package thi.cnd.careerservice.jobapplication.command.domain.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import thi.cnd.careerservice.company.CompanyPort;
import thi.cnd.careerservice.exception.IdentifiedRuntimeException;
import thi.cnd.careerservice.jobapplication.command.application.model.JobApplicationCommand.CreateJobApplication;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplication;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationId;
import thi.cnd.careerservice.jobapplication.query.port.JobApplicationQueryPort;
import thi.cnd.careerservice.joboffer.query.port.JobOfferQueryPort;
import thi.cnd.careerservice.user.UserPort;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferStatus;

import static thi.cnd.careerservice.exception.BasicErrorCode.CONFLICTING_ACTION;
import static thi.cnd.careerservice.exception.BasicErrorCode.GENERIC_INPUT_ERROR;

@Component
public class JobApplicationDomainService {

    private final JobOfferQueryPort jobOfferQueryPort;
    private final CompanyPort companyPort;
    private final UserPort userPort;
    private final JobApplicationQueryPort jobApplicationQueryPort;
    private final int maxJobApplicationPerNonSubscribedUser;

    public JobApplicationDomainService(JobOfferQueryPort jobOfferQueryPort, CompanyPort companyPort, UserPort userPort,
        JobApplicationQueryPort jobApplicationQueryPort,
        @Value("${jobapplication.maxJobApplicationPerNonSubscribedUser}") int maxJobApplicationPerNonSubscribedUser) {
        this.jobOfferQueryPort = jobOfferQueryPort;
        this.companyPort = companyPort;
        this.userPort = userPort;
        this.jobApplicationQueryPort = jobApplicationQueryPort;
        this.maxJobApplicationPerNonSubscribedUser = maxJobApplicationPerNonSubscribedUser;
    }


    public JobApplication createJobApplication(CreateJobApplication command) {

        try {
            var jobOfferFuture = CompletableFuture.supplyAsync(() -> jobOfferQueryPort.getJobOffer(command.jobOfferId()));
            var companyFuture = CompletableFuture.supplyAsync(() ->
                companyPort.getCompany(command.companyId())
            );
            var userFuture = CompletableFuture.supplyAsync(() ->
                userPort.getUser(command.userId())
            );
            var jobApplicationFuture = CompletableFuture.supplyAsync(() ->
                jobApplicationQueryPort.countAllJobApplicationsForUserId(command.userId())
            );

            CompletableFuture.allOf(jobOfferFuture, companyFuture, userFuture, jobApplicationFuture).get();
            var jobOffer = jobOfferFuture.get();
            var company = companyFuture.get();
            var user = userFuture.get();
            var jobApplicationCount = jobApplicationFuture.get();

            if (jobOffer.getStatus() != JobOfferStatus.OPEN) {
                throw new IdentifiedRuntimeException(GENERIC_INPUT_ERROR,
                    () -> "Cannot create job application if job offer is not in open status.");
            }

            if (!user.isSubscribed() && maxJobApplicationPerNonSubscribedUser < jobApplicationCount) {
                throw new IdentifiedRuntimeException(CONFLICTING_ACTION, () -> "User exceeds max amount of job applications.");
            }

            return JobApplication.init().create(
                new JobApplicationId(jobOffer.getId(), user.id()),
                command.userId(),
                jobOffer,
                company.id(),
                company.name(),
                company.location(),
                command.status(),
                command.content()
            );

        } catch (ExecutionException e) {
            if (e.getCause() instanceof IdentifiedRuntimeException exception) {
                throw exception;
            }
            throw new IdentifiedRuntimeException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IdentifiedRuntimeException(e);
        }
    }

}
