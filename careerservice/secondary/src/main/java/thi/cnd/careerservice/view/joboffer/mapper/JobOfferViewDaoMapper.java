package thi.cnd.careerservice.view.joboffer.mapper;

import thi.cnd.careerservice.joboffer.view.model.JobOfferView;
import thi.cnd.careerservice.view.joboffer.model.JobOfferViewDAO;

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
