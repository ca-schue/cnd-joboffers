package thi.cnd.userservice.core.model.user;


import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import thi.cnd.userservice.core.model.company.CompanyId;
import thi.cnd.userservice.core.exception.UserAlreadyMemberOfCompanyException;
import thi.cnd.userservice.core.exception.UserNotInvitedException;

import java.util.Set;

@Validated
@Getter
@Setter
@AllArgsConstructor
public class UserCompanyAssociation {
    @NotNull Set<CompanyId> memberOf;
    @NotNull Set<CompanyId> invitedTo;
    @Nullable CompanyId ownerOf;

    public UserCompanyAssociation() {
        this(Set.of(), Set.of(), null);
    }

    public void addOwnerOfCompany(CompanyId companyId) {
        ownerOf = CompanyId.of(companyId.toString());
        addAsMemberOfCompany(companyId);
    }

    public void inviteTo(@NotNull CompanyId companyId) throws UserAlreadyMemberOfCompanyException {
        if (memberOf.contains(companyId)) {
            throw new UserAlreadyMemberOfCompanyException(companyId);
        }
        invitedTo.add(companyId);
    }

    public void acceptCompanyInvitation(@NotNull CompanyId companyId) throws UserNotInvitedException {
        if (!invitedTo.contains(companyId)) {
            throw new UserNotInvitedException(companyId);
        }
        addAsMemberOfCompany(companyId);
    }

    private void addAsMemberOfCompany(CompanyId companyId) {
        memberOf.add(companyId);
        invitedTo.remove(companyId);
    }

    public void removeInvitation(@NotNull CompanyId companyId) throws UserNotInvitedException {
        if (!invitedTo.contains(companyId)) {
            throw new UserNotInvitedException(companyId);
        }
        invitedTo.remove(companyId);
    }

}
