package thi.cnd.userservice.core.domain;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import thi.cnd.userservice.core.model.company.Company;
import thi.cnd.userservice.core.model.company.CompanyDetails;
import thi.cnd.userservice.core.model.company.CompanyId;
import thi.cnd.userservice.core.model.company.CompanyLinks;
import thi.cnd.userservice.core.model.user.UserId;
import thi.cnd.userservice.core.ports.primary.CompanyServicePort;

import java.util.Set;
import java.util.function.Consumer;

@Service
@AllArgsConstructor
public class CompanyService implements CompanyServicePort {

    @Override
    public Company findCompanyById(CompanyId companyId) {
        return null;
    }

    @Override
    public Page<Company> searchCompanies(@Nullable String name, Set<String> tags, Pageable pageable) {
        return null;
    }

    @Override
    public Company registerNewCompany(UserId ownerId, CompanyDetails details, CompanyLinks links) {
        return null;
    }

    @Override
    public Company updateCompanyData(CompanyId companyId, CompanyDetails updatedCompanyDetails) {
        return null;
    }

    @Override
    public boolean deleteCompanyById(CompanyId companyId) {
        return false;
    }

    @Override
    public void addMember(CompanyId id, UserId userId) {

    }
}
