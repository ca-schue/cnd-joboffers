package thi.cnd.careerservice.view.joboffer.repository;

import java.util.List;

import org.springframework.data.domain.Page;

import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.joboffer.view.model.SearchAvailableJobOfferFilter;
import thi.cnd.careerservice.view.joboffer.model.JobOfferViewDAO;

public interface JobOfferComplexQueryRepository {

    Page<JobOfferViewDAO> findAllOpen(SearchAvailableJobOfferFilter filter);

    List<JobOfferViewDAO> findByCompanyId(CompanyId companyId, boolean showOnlyOpen);

    long countByCompanyId(CompanyId companyId, boolean showOnlyOpen);

}
