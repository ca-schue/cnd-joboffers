package thi.cnd.userservice.secondary.company;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import thi.cnd.userservice.core.exception.CompanyAlreadyExistsException;
import thi.cnd.userservice.core.exception.CompanyNotFoundByIdException;
import thi.cnd.userservice.core.model.company.Company;
import thi.cnd.userservice.core.model.company.CompanyId;
import thi.cnd.userservice.core.ports.secondary.CompanyRepositoryPort;
import thi.cnd.userservice.secondary.company.model.CompanyDAO;
import thi.cnd.userservice.secondary.company.model.CompanyDaoMapper;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CompanyRepositoryAdapter implements CompanyRepositoryPort {

    private final CompanyMongoDBRepository repository;
    private final CompanyDaoMapper mapper;

    @Override
    public Company findCompanyById(CompanyId companyId) throws CompanyNotFoundByIdException {
        return repository
                .findById(companyId)
                .map(mapper::toCompany)
                .orElseThrow(() -> new CompanyNotFoundByIdException(companyId));
    }

    @Override
    public Company updateOrSaveCompany(Company updatedCompany) {
        try {
            findCompanyById(updatedCompany.getId());
        } catch (CompanyNotFoundByIdException e) {
            throw new RuntimeException("No company with id " + updatedCompany.getId() + " to update."); // TODO: better solution?
        }
        CompanyDAO updatedCompanyDao = mapper.toDAO(updatedCompany);
        CompanyDAO savedUpdatedCompanyDao = repository.save(updatedCompanyDao); // put & post
        return mapper.toCompany(savedUpdatedCompanyDao);
    }

    @Override
    public Page<Company> searchCompaniesByName(@Nullable String name, Set<String> tags, Pageable pageable) {
        if (name == null && tags.isEmpty()) {
            return repository.findAll(pageable).map(mapper::toCompany);
        }
        return repository.findByDetailsNameContainingIgnoreCaseAndDetailsTagsContainingIgnoreCase(name, tags, pageable)
                .map(mapper::toCompany);
    }

    @Override
    public Company saveCompany(Company company) throws CompanyAlreadyExistsException {
        try {
            var dao = mapper.toDAO(company);
            var savedEntity = repository.save(dao);
            return mapper.toCompany(savedEntity);
        } catch (DuplicateKeyException e) {
            throw new CompanyAlreadyExistsException("Company with id " + company.getId().toString() + " already exists.");
        }
    }

    @Override
    public void deleteCompanyById(CompanyId companyId) throws CompanyNotFoundByIdException {
        repository.deleteOneById(companyId).orElseThrow(() -> new CompanyNotFoundByIdException(companyId));
    }
}
