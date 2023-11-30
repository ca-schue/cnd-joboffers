package thi.cnd.careerservice.jobapplication.command.domain.model;

import java.util.List;
import java.util.UUID;

import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplication;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationId;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationStatus;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferId;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferStatus;
import thi.cnd.careerservice.joboffer.query.domain.model.JobOfferView;
import thi.cnd.careerservice.shared.view.model.ViewEventMetadata;
import thi.cnd.careerservice.user.model.UserId;

import static thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationStatus.OPEN;


public class JobApplicationTestProvider {

    private JobApplicationTestProvider() {
    }

    public static UserId USER_ID = new UserId(UUID.fromString("04aeba22-918d-4b1e-b79f-8d6d90a07f32"));
    public static JobOfferId JOB_OFFER_ID = new JobOfferId(UUID.fromString("8a462b8f-472b-4ea7-ad40-34ccd304d090"));
    public static CompanyId COMPANY_ID = new CompanyId(UUID.fromString("c64e7065-b09a-4024-ba02-27e3bb6844e9"));
    public static UserId CREATED_BY = new UserId(UUID.fromString("e5470dc3-a03b-4086-a2db-07889f6583fb"));
    public static String COMPANY_NAME = "";
    public static String COMPANY_LOCATION = "";
    public static String CONTENT = "";

    public static JobApplication createOpenJobApplication() {
        return createJobApplication(OPEN);
    }

    public static JobApplication createJobApplication(JobApplicationStatus status) {
        return createJobApplication(status, null);
    }

    public static JobApplication createJobApplication(JobApplicationStatus status, JobOfferId jobOfferId) {
        return JobApplication.init()
            .create(
                new JobApplicationId(),
                USER_ID,
                new JobOfferView(
                    jobOfferId != null ? jobOfferId : JOB_OFFER_ID,
                    COMPANY_ID,
                    CREATED_BY,
                    "",
                    "",
                    JobOfferStatus.OPEN,
                    List.of(),
                    null,
                    new ViewEventMetadata(0, 0)
                ),
                COMPANY_ID,
                COMPANY_NAME,
                COMPANY_LOCATION,
                status != null ? status : OPEN,
                CONTENT
            );
    }

}
