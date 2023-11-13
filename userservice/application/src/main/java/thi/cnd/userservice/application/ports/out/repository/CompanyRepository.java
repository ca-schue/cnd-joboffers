package thi.cnd.userservice.application.ports.out.repository;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import thi.cnd.userservice.domain.exceptions.CompanyAlreadyExistsException;
import thi.cnd.userservice.domain.exceptions.CompanyNotFoundByIdException;
import thi.cnd.userservice.domain.model.company.*;
import thi.cnd.userservice.domain.model.user.*;

import java.util.List;
import java.util.Set;

public interface CompanyRepository {

    List<Company> findAllCompaniesWithUserMember(@NotNull UserId userId);

    @NotNull Company findCompanyById(@NotNull CompanyId companyId) throws CompanyNotFoundByIdException;

    @NotNull Company updateOrSaveCompany(Company updatedCompany); // TODO: do like Auth service => strict update!

    Page<Company> searchCompaniesByName(@Nullable String name, @NotNull Set<String> tags, Pageable pageable);

    @NotNull Company saveCompany(@NotNull Company company) throws CompanyAlreadyExistsException;

    void deleteCompanyById(@NotNull CompanyId companyId) throws CompanyNotFoundByIdException;
}
