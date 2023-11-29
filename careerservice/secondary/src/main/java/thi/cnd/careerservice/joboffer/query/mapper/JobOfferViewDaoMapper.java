package thi.cnd.careerservice.joboffer.query.mapper;

import thi.cnd.careerservice.joboffer.query.domain.model.JobOfferView;
import thi.cnd.careerservice.joboffer.query.model.JobOfferViewDAO;

public class JobOfferViewDaoMapper {

    private JobOfferViewDaoMapper() {}

    public static JobOfferViewDAO toDAO(JobOfferView jobOffer) {
        return new JobOfferViewDAO(
            jobOffer.getId(),
            jobOffer.getCompanyId(),
            jobOffer.getCreatedBy(),
            jobOffer.getTitle(),
            jobOffer.getDescription(),
            jobOffer.getStatus(),
            jobOffer.getTags(),
            jobOffer.getSalaryRange(),
            jobOffer.getMetadata()
        );
    }

    public static JobOfferView toJobOffer(JobOfferViewDAO dao) {
        return new JobOfferView(
            dao.id(),
            dao.companyId(),
            dao.createdBy(),
            dao.title(),
            dao.description(),
            dao.status(),
            dao.tags(),
            dao.salaryRange(),
            dao.metadata()
        );
    }

}
