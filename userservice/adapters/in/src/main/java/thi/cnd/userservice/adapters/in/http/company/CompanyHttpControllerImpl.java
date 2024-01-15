package thi.cnd.userservice.adapters.in.http.company;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import thi.cnd.userservice.adapters.generated.rest.CompanyApi;
import thi.cnd.userservice.adapters.generated.rest.model.*;
import thi.cnd.userservice.adapters.in.http.HttpErrorException;
import thi.cnd.userservice.domain.exceptions.*;
import thi.cnd.userservice.domain.model.company.*;
import thi.cnd.userservice.domain.model.user.UserId;
import thi.cnd.userservice.domain.CompanyService;
import thi.cnd.userservice.domain.UserService;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
class CompanyHttpControllerImpl implements CompanyApi {

    private final CompanyService companyService;
    private final CompanyDtoMapper companyDtoMapper;
    private final UserService userService;

    private static final int GLOBAL_MAX_RESULTS = 1000;

    @Override
    public ResponseEntity<CompanyDTO> createNewCompany(CompanyRegistrationRequestDTO requestDTO) {
        // Author. = Member
        try {
            Company company = companyService.registerNewCompany(
                    new UserId(requestDTO.getOwnerId()),
                    companyDtoMapper.toCompanyDetails(requestDTO.getDetails()),
                    requestDTO.getLinks() != null ? companyDtoMapper.toCompanyLinks(requestDTO.getLinks()) : new CompanyLinks()
            );
            return ResponseEntity.ok(companyDtoMapper.toCompanyDTO(company));
        } catch (UserNotFoundByIdException e) {
            throw new HttpErrorException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (CompanyAlreadyExistsException | UserAlreadyOwnerOfCompanyException e) {
            throw new HttpErrorException(HttpStatus.CONFLICT, e.getMessage());
        } catch (InvalidArgumentException e4) {
            throw new HttpErrorException(HttpStatus.BAD_REQUEST, e4.getMessage());
        } catch (ConstraintViolationException e5) {
            throw new HttpErrorException(HttpStatus.BAD_REQUEST, "Please enter valid inputs.");
        }
    }

    @Override
    public ResponseEntity<Void> deleteCompany(UUID companyId) {
        // Author. = Owner
        try {
            companyService.deleteCompanyById(new CompanyId(companyId));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (CompanyNotFoundByIdException | UserNotFoundByIdException e) {
            throw new HttpErrorException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<CompanyDTO> getCompany(UUID companyId) {
        // Author. = member
        try {
            Company company = companyService.findCompanyById(new CompanyId(companyId));
            return ResponseEntity.ok(companyDtoMapper.toCompanyDTO(company));
        } catch (CompanyNotFoundByIdException e) {
            throw new HttpErrorException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<PublicCompanyProfileDTO> getPublicCompanyProfile(UUID companyId) {
        // Author. = offen
        try {
            Company company = companyService.findCompanyById(new CompanyId(companyId));
            return ResponseEntity.ok(companyDtoMapper.toPublicProfileDTO(company));
        } catch (CompanyNotFoundByIdException e) {
            throw new HttpErrorException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Void> inviteUserToCompany(UUID companyId, CompanyInviteUserRequestDTO requestDTO) {
        // Author. = Member
        try {
            userService.inviteUserToCompany(new CompanyId(companyId), requestDTO.getUserProfileEmail());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserNotFoundByEmailException | UserNotFoundByIdException | CompanyNotFoundByIdException e1) {
            throw new HttpErrorException(HttpStatus.NOT_FOUND, e1.getMessage());
        } catch (UserAlreadyMemberOfCompanyException e2) {
            throw new HttpErrorException(HttpStatus.CONFLICT, e2.getMessage());
        }
    }

    @Override
    public ResponseEntity<CompanyDTO> subscribeToPartnerProgram(UUID companyId) {
        try {
            Company subscribedCompany = companyService.subscribeToPartnerProgram(CompanyId.of(companyId.toString()));
            return ResponseEntity.ok(companyDtoMapper.toCompanyDTO(subscribedCompany));
        } catch (CompanyAlreadyPartnerProgramSubscriberException e) {
            throw new HttpErrorException(HttpStatus.CONFLICT, e.getMessage());
        } catch (CompanyNotFoundByIdException e) {
            throw new HttpErrorException(HttpStatus.NOT_FOUND, e.getMessage());
        }
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
        // Author. = Member
        try {
            Company company = companyService.updateCompanyDetails(
                    new CompanyId(companyId),
                    new CompanyDetails(
                            requestDTO.getName(),
                            requestDTO.getDescription(),
                            requestDTO.getTags(),
                            requestDTO.getLocation()
                        )
                    );
            return ResponseEntity.ok(companyDtoMapper.toCompanyDTO(company));
        } catch (CompanyNotFoundByIdException e) {
            throw new HttpErrorException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InvalidArgumentException e2) {
            throw new HttpErrorException(HttpStatus.BAD_REQUEST, e2.getMessage());
        } catch (ConstraintViolationException e3) {
            throw new HttpErrorException(HttpStatus.BAD_REQUEST, "Please enter valid inputs.");
        }
    }

    @Override
    public ResponseEntity<CompanyDTO> updateCompanyLinks(UUID companyId, UpdateCompanyLinksRequestDTO requestDTO) {
        // Author. = Member
        try {
            Company company = companyService.updateCompanyLinks(
                    new CompanyId(companyId),
                    new CompanyLinks(
                            requestDTO.getHomepage(),
                            requestDTO.getSocialMedia()
                    )
            );
            return ResponseEntity.ok(companyDtoMapper.toCompanyDTO(company));
        } catch (CompanyNotFoundByIdException e) {
            throw new HttpErrorException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (ConstraintViolationException e4) {
            throw new HttpErrorException(HttpStatus.BAD_REQUEST, "Please enter valid inputs.");
        }
    }
}
