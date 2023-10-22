package thi.cnd.userservice.core.port.secondary;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import thi.cnd.userservice.core.exception.CompanyAlreadyExistsException;
import thi.cnd.userservice.core.exception.CompanyNotFoundByIdException;
import thi.cnd.userservice.core.model.company.Company;
import thi.cnd.userservice.core.model.company.CompanyId;

import java.util.Set;

public interface CompanyRepositoryPort {

    @NotNull Company findCompanyById(@NotNull CompanyId companyId) throws CompanyNotFoundByIdException;

    @NotNull Company updateOrSaveCompany(Company updatedCompany);

    Page<Company> searchCompaniesByName(@Nullable String name, @NotNull Set<String> tags, Pageable pageable);

    @NotNull Company saveCompany(@NotNull Company company) throws CompanyAlreadyExistsException;

    void deleteCompanyById(@NotNull CompanyId companyId) throws CompanyNotFoundByIdException;
}
