package thi.cnd.userservice.core.model.company;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;
import thi.cnd.userservice.core.model.user.UserId;

import java.util.Set;

@Getter
@AllArgsConstructor
@Validated
public class Company {

    @NotNull
    private CompanyId id;
    @NotNull
    private UserId owner;
    @NotNull
    private Set<UserId> members;
    @NotNull
    private CompanyDetails details;
    @NotNull
    private CompanyLinks links;
    @NotNull
    private CompanyPartnerProgram partnerProgram;

    public Company(@NotNull UserId creator, @NotNull CompanyDetails details, @NotNull CompanyLinks links) {
        this(
            new CompanyId(),
            creator,
            Set.of(creator),
            details,
            links,
            new CompanyPartnerProgram()
        );
    }

    public Company updateCompanyDetails(@NotNull CompanyDetails details) {
        this.details = details;
        return this;
    }

    public Company updateCompanyLinks(@NotNull CompanyLinks links) {
        this.links = links;
        return this;
    }

    public Company addMember(UserId userId) {
        members.add(userId);
        return this;
    }
}
