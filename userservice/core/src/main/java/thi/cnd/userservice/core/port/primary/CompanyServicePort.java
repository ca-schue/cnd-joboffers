package thi.cnd.userservice.core.port.primary;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import thi.cnd.userservice.core.exception.*;
import thi.cnd.userservice.core.model.company.*;
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
    ) throws UserNotFoundByIdException, CompanyAlreadyExistsException, UserAlreadyOwnerOfCompanyException;

    @NotNull Company updateCompanyDetails(
            @NotNull CompanyId companyId,
            @NotNull CompanyDetails updatedCompanyDetails
    ) throws CompanyNotFoundByIdException;

    @NotNull Company updateCompanyLinks(
            @NotNull CompanyId companyId,
            @NotNull CompanyLinks updatedCompanyLinks
    ) throws CompanyNotFoundByIdException;

    @NotNull Company subscribeToPartnerProgram(
            @NotNull CompanyId companyId
    ) throws CompanyNotFoundByIdException, CompanyAlreadyPartnerProgramSubscriberException;

    void deleteCompanyById(@NotNull CompanyId companyId) throws CompanyNotFoundByIdException, UserNotFoundByIdException;

    @NotNull Company addMember(CompanyId companyId, UserId userId) throws CompanyNotFoundByIdException;
}