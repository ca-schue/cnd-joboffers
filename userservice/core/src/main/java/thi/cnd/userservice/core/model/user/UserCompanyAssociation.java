package thi.cnd.userservice.core.model.user;


import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import thi.cnd.userservice.core.model.company.CompanyId;
import thi.cnd.userservice.core.exception.UserAlreadyMemberOfCompanyException;
import thi.cnd.userservice.core.exception.UserNotInvitedException;

import java.util.Set;

@Validated
public record UserCompanyAssociation(
        @NotNull Set<CompanyId> memberOf,
        @NotNull Set<CompanyId> invitedTo
) {

    public UserCompanyAssociation() {
        this(Set.of(), Set.of());
    }

    public void addOwnerOfCompany(CompanyId companyId) {
        addAsMemberOfCompany(companyId);
    }

    public void inviteTo(@NotNull CompanyId companyId) throws UserAlreadyMemberOfCompanyException {
        if (memberOf.contains(companyId)) {
            throw new UserAlreadyMemberOfCompanyException(companyId);
        }
        invitedTo.add(companyId);
    }

    public void acceptCompanyInvitation(@NotNull CompanyId companyId) throws UserNotInvitedException {
        if (!invitedTo().contains(companyId)) {
            throw new UserNotInvitedException(companyId);
        }
        addAsMemberOfCompany(companyId);
    }

    private void addAsMemberOfCompany(CompanyId companyId) {
        memberOf.add(companyId);
        invitedTo.remove(companyId);
    }

    public void removeInvitation(@NotNull CompanyId companyId) throws UserNotInvitedException {
        if (!invitedTo().contains(companyId)) {
            throw new UserNotInvitedException(companyId);
        }
        invitedTo.remove(companyId);
    }

}
