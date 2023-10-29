package thi.cnd.careerservice.view.joboffer;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import thi.cnd.careerservice.joboffer.model.JobOfferStatus;
import thi.cnd.careerservice.joboffer.view.model.SearchAvailableJobOfferFilter;
import thi.cnd.careerservice.shared.model.ETag;
import thi.cnd.careerservice.exception.IdentifiedRuntimeException;
import thi.cnd.careerservice.exception.ViewResourceNotFoundException;
import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.joboffer.model.JobOfferId;
import thi.cnd.careerservice.joboffer.view.model.JobOfferView;
import thi.cnd.careerservice.joboffer.view.query.JobOfferQueryPort;
import thi.cnd.careerservice.view.joboffer.mapper.JobOfferViewDaoMapper;
import thi.cnd.careerservice.view.joboffer.model.JobOfferViewDAO;
import thi.cnd.careerservice.view.joboffer.repository.JobOfferViewRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class JobOfferQueryAdapter implements JobOfferQueryPort {

    private final JobOfferViewRepository repository;

    @Override
    public long countAllOpenJobOffersOfCompany(CompanyId companyId) {
        return repository.countByCompanyId(companyId, true);
    }

    @Override
    public JobOfferView getJobOffer(JobOfferId id) {
        return getJobOffer(id, Optional.empty());
    }

    @Override
    public JobOfferView getJobOffer(JobOfferId id, Optional<ETag> eTag) {
        return getJobOffer(id, eTag, false);
    }

    @Override
    public JobOfferView getAvailableJobOffer(JobOfferId id, Optional<ETag> eTag) {
        return getJobOffer(id, eTag, true);
    }

    private JobOfferView getJobOffer(JobOfferId id, Optional<ETag> eTag, boolean mustBeInStateOpen) {
        Optional<JobOfferViewDAO> searchedDao = repository.findById(id);

        var dao = searchedDao.orElseThrow(() -> new ViewResourceNotFoundException("JobOffer", id));

        if (mustBeInStateOpen && dao.status() != JobOfferStatus.OPEN) {
            throw new ViewResourceNotFoundException("JobOffer", id);
        }

        if (eTag.isPresent()) {
            var requiredVersion = eTag.get().getValue();
            if (dao.metadata().version() <= requiredVersion) {
                throw new IdentifiedRuntimeException(HttpStatus.NOT_MODIFIED);
            }
        }

        return JobOfferViewDaoMapper.toJobOffer(dao);
    }

    @Override
    public List<JobOfferView> getAvailableJobOffersOfCompany(CompanyId id) {
        return repository.findByCompanyId(id, false).stream().map(JobOfferViewDaoMapper::toJobOffer).toList();
    }

    @Override
    public List<JobOfferView> getAllJobOffersOfCompany(CompanyId id) {
        return repository.findByCompanyId(id, false).stream().map(JobOfferViewDaoMapper::toJobOffer).toList();
    }

    @Override
    public Page<JobOfferView> searchAllOpenJobOffers(SearchAvailableJobOfferFilter filter) {
        return repository.findAllOpen(filter).map(JobOfferViewDaoMapper::toJobOffer);
    }

}
