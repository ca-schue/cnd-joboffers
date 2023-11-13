package thi.cnd.userservice.application.ports.out.event;

import thi.cnd.userservice.domain.model.company.CompanyId;
import thi.cnd.userservice.domain.model.user.User;

public interface CompanyEvents {
    void publishCompanyDeleted(CompanyId companyId);
    void publishCompanyRegistered(CompanyId registeredCompanyId, User owner, String companyName);
    void publishCompanyNameChanged(CompanyId companyId, String updatedCompanyName);
}
