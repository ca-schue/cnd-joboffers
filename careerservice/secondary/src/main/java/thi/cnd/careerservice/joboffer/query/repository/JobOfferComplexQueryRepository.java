package thi.cnd.careerservice.joboffer.query.repository;

import java.util.List;

import org.springframework.data.domain.Page;

import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.joboffer.query.domain.model.AvailableJobOfferSearchFilter;
import thi.cnd.careerservice.joboffer.query.model.JobOfferViewDAO;

public interface JobOfferComplexQueryRepository {

    Page<JobOfferViewDAO> findAllOpen(AvailableJobOfferSearchFilter filter);

    List<JobOfferViewDAO> findByCompanyId(CompanyId companyId, boolean showOnlyOpen);

    long countByCompanyId(CompanyId companyId, boolean showOnlyOpen);

}
