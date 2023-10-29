package thi.cnd.careerservice.view.jobapplication.mapper;

import thi.cnd.careerservice.jobapplication.view.model.JobApplicationView;
import thi.cnd.careerservice.view.jobapplication.model.JobApplicationViewDAO;

public class JobApplicationViewDaoMapper {

    private JobApplicationViewDaoMapper() {}

    public static JobApplicationViewDAO toDAO(JobApplicationView jobApplication) {
        return new JobApplicationViewDAO(
            jobApplication.getId(),
            jobApplication.getCompany(),
            jobApplication.getJobOffer(),
            jobApplication.getUserId(),
            jobApplication.getStatus(),
            jobApplication.getContent(),
            jobApplication.getMetadata()
        );
    }

    public static JobApplicationView toJobApplication(JobApplicationViewDAO dao) {
        return new JobApplicationView(
            dao.id(),
            dao.company(),
            dao.jobOffer(),
            dao.userId(),
            dao.status(),
            dao.content(),
            dao.metadata()
        );
    }

}
