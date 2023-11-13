package thi.cnd.userservice.adapters.out.repository.company;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import thi.cnd.userservice.adapters.out.repository.company.DAOs.CompanyDAO;
import thi.cnd.userservice.adapters.out.repository.company.DAOs.CompanyDaoMapper;
import thi.cnd.userservice.domain.exceptions.CompanyAlreadyExistsException;
import thi.cnd.userservice.domain.exceptions.CompanyNotFoundByIdException;
import thi.cnd.userservice.domain.model.company.Company;
import thi.cnd.userservice.domain.model.company.CompanyId;
import thi.cnd.userservice.domain.model.user.UserId;
import thi.cnd.userservice.application.ports.out.repository.CompanyRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyMongoRepositoryImpl implements CompanyRepository {

    private final CustomCompanyMongoRepository repository;
    private final CompanyDaoMapper companyDaoMapper;


    @Override
    public List<Company> findAllCompaniesWithUserMember(UserId memberId) {
        return repository.findByMembersContains(memberId)
                .stream().map(companyDaoMapper::toCompany).collect(Collectors.toList());
    }

    @Override
    public Company findCompanyById(CompanyId companyId) throws CompanyNotFoundByIdException {
        return repository
                .findById(companyId)
                .map(companyDaoMapper::toCompany)
                .orElseThrow(() -> new CompanyNotFoundByIdException(companyId));
    }

    @Override
    public Company updateOrSaveCompany(Company updatedCompany) {
        try {
            findCompanyById(updatedCompany.getId());
        } catch (CompanyNotFoundByIdException e) {
            throw new RuntimeException("No company with id " + updatedCompany.getId() + " to update."); // TODO: better solution?
        }
        CompanyDAO updatedCompanyDao = companyDaoMapper.toDAO(updatedCompany);
        CompanyDAO savedUpdatedCompanyDao = repository.save(updatedCompanyDao); // put & post
        return companyDaoMapper.toCompany(savedUpdatedCompanyDao);
    }

    @Override
    public Page<Company> searchCompaniesByName(@Nullable String name, Set<String> tags, Pageable pageable) {
        if (name == null && tags.isEmpty()) {
            return repository.findAll(pageable).map(companyDaoMapper::toCompany);
        }
        return repository.findByDetailsNameContainingIgnoreCaseAndDetailsTagsContainingIgnoreCase(name, tags, pageable)
                .map(companyDaoMapper::toCompany);
    }

    @Override
    public Company saveCompany(Company company) throws CompanyAlreadyExistsException {
        try {
            var dao = companyDaoMapper.toDAO(company);
            var savedEntity = repository.save(dao);
            return companyDaoMapper.toCompany(savedEntity);
        } catch (DuplicateKeyException e) {
            throw new CompanyAlreadyExistsException("Company with id " + company.getId().toString() + " already exists.");
        }
    }

    @Override
    public void deleteCompanyById(CompanyId companyId) throws CompanyNotFoundByIdException {
        repository.deleteOneById(companyId).orElseThrow(() -> new CompanyNotFoundByIdException(companyId));
    }
}
