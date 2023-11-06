package thi.cnd.careerservice.joboffer.view.query;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.joboffer.view.model.SearchAvailableJobOfferFilter;
import thi.cnd.careerservice.shared.model.ETag;
import thi.cnd.careerservice.joboffer.model.JobOfferId;
import thi.cnd.careerservice.joboffer.view.model.JobOfferView;

public interface JobOfferQueryPort {

    long countAllOpenJobOffersOfCompany(CompanyId companyId);

    /**
     * Returns job offer for id
     */
    JobOfferView getJobOffer(JobOfferId id);

    /**
     * Returns job offer if etag is equal or lower than current version
     */
    JobOfferView getJobOffer(JobOfferId id, Optional<ETag> eTag);

    /**
     * Get job offer if its in state open and etag version is equal or lower
     */
    JobOfferView getAvailableJobOffer(JobOfferId id, Optional<ETag> eTag);

    /**
     * Get open job offers for company
     */
    List<JobOfferView> getAvailableJobOffersOfCompany(CompanyId id);

    /**
     * Get all job offers for company
     */
    List<JobOfferView> getAllJobOffersOfCompany(CompanyId id);

    /**
     * Search all open job offers with provided filter
     */
    Page<JobOfferView> searchAllOpenJobOffers(SearchAvailableJobOfferFilter filter);
}
