package thi.cnd.careerservice.joboffer.query.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import thi.cnd.careerservice.joboffer.query.port.JobOfferQueryPort;
import thi.cnd.careerservice.joboffer.query.domain.model.AvailableJobOfferSearchFilter;
import thi.cnd.careerservice.shared.model.ETag;
import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferId;
import thi.cnd.careerservice.joboffer.query.domain.model.JobOfferView;

@Service
public class DefaultJobOfferQueryService implements JobOfferQueryService {

    private final JobOfferQueryPort port;

    public DefaultJobOfferQueryService(JobOfferQueryPort port) {
        this.port = port;
    }

    @Override
    public JobOfferView getJobOffer(JobOfferId id, Optional<ETag> eTag) {
        return port.getJobOffer(id, eTag);
    }

    @Override
    public JobOfferView getAvailableJobOffer(JobOfferId id, Optional<ETag> eTag) {
        return port.getAvailableJobOffer(id, eTag);
    }

    @Override
    public Page<JobOfferView> searchAvailableJobOffers(AvailableJobOfferSearchFilter filter) {
        return port.searchAllOpenJobOffers(filter);
    }

    @Override
    public List<JobOfferView> getAvailableJobOffersByCompanyId(CompanyId id) {
        return port.getAvailableJobOffersOfCompany(id);
    }

    @Override
    public List<JobOfferView> getAllJobOffersByCompanyId(CompanyId id) {
        return port.getAllJobOffersOfCompany(id);
    }

}
