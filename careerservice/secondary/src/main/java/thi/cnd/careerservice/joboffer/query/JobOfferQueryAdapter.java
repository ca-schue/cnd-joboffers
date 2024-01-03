package thi.cnd.careerservice.joboffer.query;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferStatus;
import thi.cnd.careerservice.joboffer.query.mapper.JobOfferViewDaoMapper;
import thi.cnd.careerservice.joboffer.query.model.JobOfferViewDAO;
import thi.cnd.careerservice.joboffer.query.domain.model.AvailableJobOfferSearchFilter;
import thi.cnd.careerservice.joboffer.query.repository.JobOfferViewRepository;
import thi.cnd.careerservice.shared.model.ETag;
import thi.cnd.careerservice.exception.IdentifiedRuntimeException;
import thi.cnd.careerservice.exception.ViewResourceNotFoundException;
import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferId;
import thi.cnd.careerservice.joboffer.query.domain.model.JobOfferView;
import thi.cnd.careerservice.joboffer.query.port.JobOfferQueryPort;
import lombok.RequiredArgsConstructor;

import static thi.cnd.careerservice.exception.BasicErrorCode.RESOURCE_NOT_MODIFIED;

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
                throw new IdentifiedRuntimeException(RESOURCE_NOT_MODIFIED);
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
    public Page<JobOfferView> searchAllOpenJobOffers(AvailableJobOfferSearchFilter filter) {
        return repository.findAllOpen(filter).map(JobOfferViewDaoMapper::toJobOffer);
    }

}
