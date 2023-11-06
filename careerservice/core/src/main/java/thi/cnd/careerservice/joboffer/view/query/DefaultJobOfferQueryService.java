package thi.cnd.careerservice.joboffer.view.query;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import thi.cnd.careerservice.joboffer.view.model.SearchAvailableJobOfferFilter;
import thi.cnd.careerservice.shared.model.ETag;
import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.joboffer.model.JobOfferId;
import thi.cnd.careerservice.joboffer.view.model.JobOfferView;

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
    public Page<JobOfferView> searchAvailableJobOffers(SearchAvailableJobOfferFilter filter) {
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
