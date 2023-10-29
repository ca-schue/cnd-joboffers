package thi.cnd.careerservice.joboffer.view.query;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.joboffer.model.JobOfferId;
import thi.cnd.careerservice.joboffer.view.model.SearchAvailableJobOfferFilter;
import thi.cnd.careerservice.joboffer.view.model.JobOfferView;
import thi.cnd.careerservice.shared.model.ETag;

@Validated
public interface JobOfferQueryService {

    /**
     * Search all open job offers.
     */
    @NotNull Page<JobOfferView> searchAvailableJobOffers(SearchAvailableJobOfferFilter filter);

    /**
     * Return job offer for id, if etag is not matching.
     */
    @NotNull JobOfferView getJobOffer(@NotNull JobOfferId id, @NotNull Optional<ETag> eTag);

    /**
     * Return job offer for id, if its in state open and etag is not matching.
     */
    @NotNull JobOfferView getAvailableJobOffer(@NotNull JobOfferId id, @NotNull Optional<ETag> eTag);

    /**
     * Return all open job offers for company
     */
    @NotNull List<JobOfferView> getAvailableJobOffersByCompanyId(@NotNull CompanyId id);

    /**
     * Return all job offers for company
     */
    @NotNull List<JobOfferView> getAllJobOffersByCompanyId(@NotNull CompanyId id);

}
