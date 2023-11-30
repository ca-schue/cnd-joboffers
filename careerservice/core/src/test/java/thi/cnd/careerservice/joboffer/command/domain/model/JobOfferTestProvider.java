package thi.cnd.careerservice.joboffer.command.domain.model;

import java.util.List;
import java.util.UUID;

import org.javamoney.moneta.Money;

import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOffer;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferStatus;
import thi.cnd.careerservice.joboffer.command.domain.model.SalaryRange;
import thi.cnd.careerservice.user.model.UserId;

public class JobOfferTestProvider {

    private JobOfferTestProvider() {}

    public static CompanyId COMPANY_ID = new CompanyId(UUID.fromString("4953a302-270a-4b80-a14c-d8c814da30f4"));
    public static UserId CREATED_BY = new UserId(UUID.fromString("b7edd62f-78cd-48b9-b865-ef204b1f2778"));
    public static String TITLE = "Title";
    public static String DESCRIPTION = "Description";
    public static List<String> TAGS =  List.of("Tag1");
    public static Money SALARY_LOWER_BOUND = Money.of(50000, "EUR");
    public static Money SALARY_UPPER_BOUND = Money.of(100000, "EUR");
    public static SalaryRange SALARY_RANGE = new SalaryRange(SALARY_LOWER_BOUND, SALARY_UPPER_BOUND);

    public static JobOffer createOpenJobOffer() {
        return createJobOffer(JobOfferStatus.OPEN);
    }

    public static JobOffer createJobOffer(JobOfferStatus status) {
        return JobOffer.init().create(
            COMPANY_ID,
            CREATED_BY,
            TITLE,
            DESCRIPTION,
            status,
            TAGS,
            SALARY_RANGE
        );
    }


}
