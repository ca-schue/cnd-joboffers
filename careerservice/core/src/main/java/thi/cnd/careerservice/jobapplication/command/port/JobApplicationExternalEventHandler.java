package thi.cnd.careerservice.jobapplication.command.port;

import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.user.model.UserId;

public interface JobApplicationExternalEventHandler {

    void updateCompanyName(CompanyId id, String name);

    void deleteJobApplication(UserId userId);

}
