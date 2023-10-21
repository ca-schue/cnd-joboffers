package thi.cnd.userservice.core.ports.primary;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import thi.cnd.userservice.core.model.company.Company;
import thi.cnd.userservice.core.model.company.CompanyDetails;
import thi.cnd.userservice.core.model.company.CompanyId;
import thi.cnd.userservice.core.model.company.CompanyLinks;
import thi.cnd.userservice.core.model.user.UserId;

import java.util.Set;
import java.util.function.Consumer;

public interface CompanyServicePort {

    Company findCompanyById(@NotNull CompanyId companyId);

    Page<Company> searchCompanies(@Nullable String name, @NotNull Set<String> tags, Pageable pageable);

    @NotNull Company registerNewCompany(
            @NotNull UserId ownerId,
            @NotNull CompanyDetails details,
            @NotNull CompanyLinks links
    );

    @NotNull Company updateCompanyData(@NotNull CompanyId companyId, @NotNull CompanyDetails updatedCompanyDetails);

    boolean deleteCompanyById(@NotNull CompanyId companyId);

    void addMember(CompanyId id, UserId userId);
}