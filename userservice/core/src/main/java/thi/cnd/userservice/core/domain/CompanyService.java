package thi.cnd.userservice.core.domain;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import thi.cnd.userservice.core.exception.CompanyAlreadyExistsException;
import thi.cnd.userservice.core.exception.CompanyNotFoundByIdException;
import thi.cnd.userservice.core.exception.UserNotFoundByIdException;
import thi.cnd.userservice.core.model.company.Company;
import thi.cnd.userservice.core.model.company.CompanyDetails;
import thi.cnd.userservice.core.model.company.CompanyId;
import thi.cnd.userservice.core.model.company.CompanyLinks;
import thi.cnd.userservice.core.model.user.User;
import thi.cnd.userservice.core.model.user.UserId;
import thi.cnd.userservice.core.port.primary.CompanyServicePort;
import thi.cnd.userservice.core.port.secondary.event.company.CompanyDeletedEvent;
import thi.cnd.userservice.core.port.secondary.event.company.CompanyEventPort;
import thi.cnd.userservice.core.port.secondary.event.company.CompanyNameChangedEvent;
import thi.cnd.userservice.core.port.secondary.event.company.CompanyRegisteredEvent;
import thi.cnd.userservice.core.port.secondary.repository.CompanyRepositoryPort;
import thi.cnd.userservice.core.port.secondary.repository.UserRepositoryPort;

import java.util.Set;

@Service
@AllArgsConstructor
public class CompanyService implements CompanyServicePort {

    private final CompanyRepositoryPort companyRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final CompanyEventPort companyEventPort;

    @Override
    public Company findCompanyById(CompanyId companyId) throws CompanyNotFoundByIdException {
        return companyRepositoryPort.findCompanyById(companyId);
    }

    @Override
    public Page<Company> searchCompanies(@Nullable String name, Set<String> tags, Pageable pageable) {
        return null;
    }

    @Override
    public Company registerNewCompany(
            @NotNull UserId ownerId,
            @NotNull CompanyDetails details,
            @NotNull CompanyLinks links
    ) throws UserNotFoundByIdException, CompanyAlreadyExistsException {
        User owner = userRepositoryPort.findUserById(ownerId);
        Company newCompany = new Company(ownerId, details, links);
        owner.addOwnershipOfCompany(newCompany.getId());

        Company savedCompany = companyRepositoryPort.saveCompany(newCompany);
        userRepositoryPort.updateOrSaveUser(owner);
        companyEventPort.sendEvent(new CompanyRegisteredEvent(savedCompany.getId(), owner, savedCompany.getDetails().name()));
        return savedCompany;
    }

    @Override
    public @NotNull Company updateCompanyDetails(
            @NotNull CompanyId companyId,
            @NotNull CompanyDetails updatedCompanyDetails
    ) throws CompanyNotFoundByIdException {
        Company oldCompany = companyRepositoryPort.findCompanyById(companyId);

        boolean nameChanged = oldCompany.getDetails().name().equals(updatedCompanyDetails.name());

        oldCompany.setDetails(updatedCompanyDetails); // TODO: Input verification?

        if(nameChanged) {
            companyEventPort.sendEvent(new CompanyNameChangedEvent(companyId, updatedCompanyDetails.name()));
        }
        return companyRepositoryPort.updateOrSaveCompany(oldCompany);
    }

    public @NotNull Company updateCompanyLinks(
            @NotNull CompanyId companyId,
            @NotNull CompanyLinks updatedCompanylinks
    ) throws CompanyNotFoundByIdException {
        Company company = companyRepositoryPort.findCompanyById(companyId);
        company.setLinks(updatedCompanylinks); // TODO: Input verification?
        return companyRepositoryPort.updateOrSaveCompany(company);
    }

    @Override
    public void deleteCompanyById(CompanyId companyId) throws CompanyNotFoundByIdException {
        //companyRepositoryPort.deleteCompanyById(companyId);
        userRepositoryPort.removeCompanyFromUser(companyId);
        companyEventPort.sendEvent(new CompanyDeletedEvent(companyId));
    }

    @Override
    public @NotNull Company addMember(CompanyId companyId, UserId userId) throws CompanyNotFoundByIdException {
        Company company = companyRepositoryPort.findCompanyById(companyId);
        company.addMember(userId);
        return companyRepositoryPort.updateOrSaveCompany(company);
    }
}
