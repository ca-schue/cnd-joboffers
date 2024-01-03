package thi.cnd.careerservice.joboffer.command.domain.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import thi.cnd.careerservice.company.CompanyPort;
import thi.cnd.careerservice.exception.IdentifiedRuntimeException;
import thi.cnd.careerservice.exception.InvalidInputReceivedException;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOffer;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferStatus;
import thi.cnd.careerservice.joboffer.query.port.JobOfferQueryPort;
import thi.cnd.careerservice.joboffer.command.application.model.JobOfferCommand;

import static thi.cnd.careerservice.exception.BasicErrorCode.CONFLICTING_ACTION;

@Service
public class JobOfferDomainService {

    private final JobOfferQueryPort jobOfferQueryPort;
    private final CompanyPort companyPort;

    private final int maxJobOfferPerNonPartneredCompany;

    public JobOfferDomainService(
        JobOfferQueryPort jobOfferQueryPort,
        CompanyPort companyPort,
        @Value("${joboffer.maxJobOfferPerNonPartneredCompany}") int maxJobOfferPerNonPartneredCompany) {
        this.jobOfferQueryPort = jobOfferQueryPort;
        this.companyPort = companyPort;
        this.maxJobOfferPerNonPartneredCompany = maxJobOfferPerNonPartneredCompany;
    }

    public JobOffer createJobOffer(JobOfferCommand.CreateJobOffer command) {
        if (command.status() != JobOfferStatus.DRAFT && command.status() != JobOfferStatus.OPEN) {
            throw new InvalidInputReceivedException("Cannot open a job offer that is not in state 'draft' or 'open'");
        }

        var company = companyPort.getCompany(command.companyId());

        long existingJobOffer = jobOfferQueryPort.countAllOpenJobOffersOfCompany(command.companyId());


        if (!company.isPartnered() && maxJobOfferPerNonPartneredCompany < existingJobOffer) {
            throw new IdentifiedRuntimeException(CONFLICTING_ACTION, () -> "The company exceeds its max amount of open job offers");
        }

        return JobOffer.init().create(
            command.companyId(),
            command.createdBy(),
            command.title(),
            command.description(),
            command.status(),
            command.tags(),
            command.salaryRange()
        );
    }

}
