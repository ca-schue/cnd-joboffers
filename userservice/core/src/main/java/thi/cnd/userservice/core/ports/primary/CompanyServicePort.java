package thi.cnd.userservice.core.ports.primary;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import thi.cnd.userservice.core.exception.CompanyAlreadyExistsException;
import thi.cnd.userservice.core.exception.CompanyNotFoundByIdException;
import thi.cnd.userservice.core.exception.UserNotFoundByIdException;
import thi.cnd.userservice.core.model.company.Company;
import thi.cnd.userservice.core.model.company.CompanyDetails;
import thi.cnd.userservice.core.model.company.CompanyId;
import thi.cnd.userservice.core.model.company.CompanyLinks;
import thi.cnd.userservice.core.model.user.UserId;

import java.util.Set;

public interface CompanyServicePort {

    Company findCompanyById(@NotNull CompanyId companyId) throws CompanyNotFoundByIdException;

    Page<Company> searchCompanies(
            @Nullable String name,
            @NotNull Set<String> tags,
            Pageable pageable
    );

    @NotNull Company registerNewCompany(
            @NotNull UserId ownerId,
            @NotNull CompanyDetails details,
            @NotNull CompanyLinks links
    ) throws UserNotFoundByIdException, CompanyAlreadyExistsException;

    @NotNull Company updateCompanyDetails(
            @NotNull CompanyId companyId,
            @NotNull CompanyDetails updatedCompanyDetails
    ) throws CompanyNotFoundByIdException;

    @NotNull Company updateCompanyLinks(
            @NotNull CompanyId companyId,
            @NotNull CompanyLinks updatedCompanyLinks
    ) throws CompanyNotFoundByIdException;

    void deleteCompanyById(@NotNull CompanyId companyId) throws CompanyNotFoundByIdException;

    @NotNull Company addMember(CompanyId companyId, UserId userId) throws CompanyNotFoundByIdException;
}