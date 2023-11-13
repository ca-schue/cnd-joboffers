package thi.cnd.userservice.application;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import thi.cnd.userservice.domain.exceptions.*;
import thi.cnd.userservice.domain.model.company.*;
import thi.cnd.userservice.domain.model.user.*;
import thi.cnd.userservice.domain.CompanyService;
import thi.cnd.userservice.application.ports.out.event.CompanyEvents;
import thi.cnd.userservice.application.ports.out.repository.CompanyRepository;
import thi.cnd.userservice.application.ports.out.repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final CompanyEvents companyEvents;

    @Override
    public Company findCompanyById(CompanyId companyId) throws CompanyNotFoundByIdException {
        return companyRepository.findCompanyById(companyId);
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
    ) throws UserNotFoundByIdException, CompanyAlreadyExistsException, UserAlreadyOwnerOfCompanyException {
        User owner = userRepository.findUserById(ownerId);
        if (owner.getAssociations().getOwnerOf() != null) {
            throw new UserAlreadyOwnerOfCompanyException("User already owner of company '" + owner.getAssociations().getOwnerOf().toString() + "'");
        }
        Company newCompany = new Company(ownerId, details, links);
        owner.addOwnershipOfCompany(newCompany.getId());
        Company savedCompany = companyRepository.saveCompany(newCompany);
        userRepository.updateOrSaveUser(owner);
        companyEvents.publishCompanyRegistered(savedCompany.getId(), owner, savedCompany.getDetails().name());
        return savedCompany;
    }

    @Override
    public @NotNull Company updateCompanyDetails(
            @NotNull CompanyId companyId,
            @NotNull CompanyDetails updatedCompanyDetails
    ) throws CompanyNotFoundByIdException {
        Company oldCompany = companyRepository.findCompanyById(companyId);

        boolean nameChanged = oldCompany.getDetails().name().equals(updatedCompanyDetails.name());

        oldCompany.setDetails(updatedCompanyDetails); // TODO: Input verification?

        if(nameChanged) {
            companyEvents.publishCompanyNameChanged(companyId, updatedCompanyDetails.name());
        }
        return companyRepository.updateOrSaveCompany(oldCompany);
    }

    public @NotNull Company updateCompanyLinks(
            @NotNull CompanyId companyId,
            @NotNull CompanyLinks updatedCompanylinks
    ) throws CompanyNotFoundByIdException {
        Company company = companyRepository.findCompanyById(companyId);
        company.setLinks(updatedCompanylinks); // TODO: Input verification?
        return companyRepository.updateOrSaveCompany(company);
    }

    @Override
    public Company subscribeToPartnerProgram(CompanyId companyId) throws CompanyNotFoundByIdException, CompanyAlreadyPartnerProgramSubscriberException {
        Company company = companyRepository.findCompanyById(companyId);
        if (company.getPartnerProgram().isPartnered()) {
            throw new CompanyAlreadyPartnerProgramSubscriberException("Company " + company.getId() + " already subscriber to partner program until " + company.getPartnerProgram().partnerUntil());
        } else {
            company.setPartnerProgram(
                    new CompanyPartnerProgram(Instant.now().plusSeconds(60 * 60 * 24 * 30)) // 1 Month
            );
            return companyRepository.updateOrSaveCompany(company);
        }
    }

    @Override
    public void deleteUserFromAllCompanies(@NotNull UserId userId) {
        List<Company> companyList = companyRepository.findAllCompaniesWithUserMember(userId);
        for (Company company : companyList) {
            Set<UserId> members = company.getMembers();
            members.remove(userId);
            company.setMembers(members);
            companyRepository.updateOrSaveCompany(company);
        }
    }

    @Override
    public void deleteCompanyById(CompanyId companyId) throws CompanyNotFoundByIdException, UserNotFoundByIdException {
        Company companyTbd = companyRepository.findCompanyById(companyId);
        UserId ownerId = companyTbd.getOwner();
        companyRepository.deleteCompanyById(companyId);
        userRepository.removeCompanyFromUser(companyId, ownerId);
        companyEvents.publishCompanyDeleted(companyId);
    }

    @Override
    public @NotNull Company addMember(CompanyId companyId, UserId userId) throws CompanyNotFoundByIdException {
        Company company = companyRepository.findCompanyById(companyId);
        company.addMember(userId);
        return companyRepository.updateOrSaveCompany(company);
    }
}
