package thi.cnd.userservice.primary.rest.company;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import thi.cnd.userservice.api.generated.CompanyApi;
import thi.cnd.userservice.api.generated.model.*;
import thi.cnd.userservice.core.exception.*;
import thi.cnd.userservice.core.model.company.Company;
import thi.cnd.userservice.core.model.company.CompanyDetails;
import thi.cnd.userservice.core.model.company.CompanyId;
import thi.cnd.userservice.core.model.company.CompanyLinks;
import thi.cnd.userservice.core.model.user.UserId;
import thi.cnd.userservice.core.ports.primary.CompanyServicePort;
import thi.cnd.userservice.core.ports.primary.UserServicePort;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CompanyController implements CompanyApi {

    private final CompanyServicePort companyServicePort;
    private final CompanyApiMapper companyApiMapper;
    private final UserServicePort userServicePort;

    private static final int GLOBAL_MAX_RESULTS = 1000;

    @Override
    public ResponseEntity<CompanyDTO> createNewCompany(CompanyRegistrationRequestDTO requestDTO) {
        try {
            Company company = companyServicePort.registerNewCompany(
                    new UserId(requestDTO.getOwnerId()),
                    companyApiMapper.toCompanyDetails(requestDTO.getDetails()),
                    requestDTO.getLinks() != null ? companyApiMapper.toCompanyLinks(requestDTO.getLinks()) : new CompanyLinks()
            );
            return ResponseEntity.ok(companyApiMapper.toCompanyDTO(company));
        } catch (UserNotFoundByIdException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (CompanyAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Void> deleteCompany(UUID companyId) {
        try {
            companyServicePort.deleteCompanyById(new CompanyId(companyId));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (CompanyNotFoundByIdException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<CompanyDTO> getCompany(UUID companyId) {
        try {
            Company company = companyServicePort.findCompanyById(new CompanyId(companyId));
            return ResponseEntity.ok(companyApiMapper.toCompanyDTO(company));
        } catch (CompanyNotFoundByIdException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<PublicCompanyProfileDTO> getPublicCompanyProfile(UUID companyId) {
        try {
            Company company = companyServicePort.findCompanyById(new CompanyId(companyId));
            return ResponseEntity.ok(companyApiMapper.toPublicProfileDTO(company));
        } catch (CompanyNotFoundByIdException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Void> inviteUserToCompany(UUID companyId, CompanyInviteUserRequestDTO requestDTO) {
        try {
            userServicePort.inviteUserToCompany(new CompanyId(companyId), requestDTO.getUserProfileEmail());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserNotFoundByEmailException | UserNotFoundByIdException | CompanyNotFoundByIdException e1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e1.getMessage());
        } catch (UserAlreadyMemberOfCompanyException e2) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e2.getMessage());
        }
    }

    @Override
    public ResponseEntity<PaginatedPublicCompanyProfileResponseDTO> searchCompanies(
            Optional<String> name,
            Optional<Set<String>> tags,
            Optional<Integer> page,
            Optional<Integer> size) {
        var resultList = companyServicePort.searchCompanies(
                name.orElse(null),
                tags.orElse(Set.of()),
                createPageRequest(page, size)
            );
        return ResponseEntity.ok(companyApiMapper.toPaginatedCompaniesDTO(resultList));
    }

    private PageRequest createPageRequest(Optional<Integer> page, Optional<Integer> size) {
        return PageRequest.of(
                Math.max(0, page.orElse(0) - 1), // pageNumber
                Math.max(size.orElse(GLOBAL_MAX_RESULTS), Math.min(1, GLOBAL_MAX_RESULTS)), // pageSize
                Sort.unsorted() // sort strategy
        );
    }

    @Override
    public ResponseEntity<CompanyDTO> updateCompanyDetails(UUID companyId, UpdateCompanyDetailsRequestDTO requestDTO) {
        try {
            Company company = companyServicePort.updateCompanyDetails(
                    new CompanyId(companyId),
                    new CompanyDetails(
                            requestDTO.getName(),
                            requestDTO.getDescription(),
                            requestDTO.getTags(),
                            requestDTO.getLocation()
                        )
                    );
            return ResponseEntity.ok(companyApiMapper.toCompanyDTO(company));
        } catch (CompanyNotFoundByIdException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<CompanyDTO> updateCompanyLinks(UUID companyId, UpdateCompanyLinksRequestDTO requestDTO) {
        try {
            Company company = companyServicePort.updateCompanyLinks(
                    new CompanyId(companyId),
                    new CompanyLinks(
                            requestDTO.getHomepage(),
                            requestDTO.getSocialMedia()
                    )
            );
            return ResponseEntity.ok(companyApiMapper.toCompanyDTO(company));
        } catch (CompanyNotFoundByIdException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
