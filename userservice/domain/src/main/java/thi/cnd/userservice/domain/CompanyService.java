package thi.cnd.userservice.domain;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import thi.cnd.userservice.domain.exceptions.*;
import thi.cnd.userservice.domain.model.company.*;
import thi.cnd.userservice.domain.model.user.*;
import java.util.Set;

public interface CompanyService {

    Company findCompanyById(@NotNull CompanyId companyId) throws CompanyNotFoundByIdException;

    @NotNull Company registerNewCompany(
            @NotNull UserId ownerId,
            @NotNull CompanyDetails details,
            @NotNull CompanyLinks links
    ) throws UserNotFoundByIdException, CompanyAlreadyExistsException, UserAlreadyOwnerOfCompanyException, InvalidArgumentException;

    @NotNull Company updateCompanyDetails(
            @NotNull CompanyId companyId,
            @NotNull CompanyDetails updatedCompanyDetails
    ) throws CompanyNotFoundByIdException, InvalidArgumentException;

    @NotNull Company updateCompanyLinks(
            @NotNull CompanyId companyId,
            @NotNull CompanyLinks updatedCompanyLinks
    ) throws CompanyNotFoundByIdException;

    @NotNull Company subscribeToPartnerProgram(
            @NotNull CompanyId companyId
    ) throws CompanyNotFoundByIdException, CompanyAlreadyPartnerProgramSubscriberException;

    void deleteCompanyById(@NotNull CompanyId companyId) throws CompanyNotFoundByIdException, UserNotFoundByIdException;

    void deleteUserFromAllCompanies(@NotNull UserId userId);

    @NotNull Company addMember(CompanyId companyId, UserId userId) throws CompanyNotFoundByIdException;
}