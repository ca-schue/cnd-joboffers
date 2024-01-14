package thi.cnd.authservice.adapters.in.http.account;

import jakarta.validation.ConstraintViolationException;
import thi.cnd.authservice.adapters.in.http.basicAuthAccountLogin.InternalAccountDetails;
import thi.cnd.authservice.adapters.in.http.oidcAccountLogin.AuthenticatedOidcIdToken;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import thi.cnd.authservice.api.generated.AccountManagementApi;
import thi.cnd.authservice.api.generated.model.*;
import thi.cnd.authservice.domain.AccountService;
import thi.cnd.authservice.domain.exceptions.*;
import thi.cnd.authservice.domain.model.AccessToken;
import thi.cnd.authservice.domain.model.account.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
class AccountHttpControllerImpl implements AccountManagementApi {

    private final AccountService accountService;
    private final AccountDtoMapper accountDtoMapper;

    @Override
    public ResponseEntity<AccountLoginResponseDTO> registerInternalAccount(InternalAccountRegistrationRequestDTO requestDTO) {
        try {
            InternalAccount registeredInternalAccount = accountService.registerNewInternalAccount(requestDTO.getEmail(), requestDTO.getPassword());
            AccessToken accountAccessToken = accountService.mintAccountAccessToken(registeredInternalAccount);
            Jwt accountJwt = new Jwt(
                    accountAccessToken.getTokenValue(),
                    accountAccessToken.getIssuedAt(),
                    accountAccessToken.getExpiresAt(),
                    accountAccessToken.getHeaders(),
                    accountAccessToken.getClaims()
            );
            InternalAccountDTO internalAccountDTO = accountDtoMapper.toInternalDTO(registeredInternalAccount);
            return ResponseEntity.ok(accountDtoMapper.toLoginResponseDTO(internalAccountDTO, accountJwt));
        } catch (AccountAlreadyExistsException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        } catch (InvalidPasswordException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (ConstraintViolationException | InvalidEmailException e4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please enter valid inputs.");
        }
    }

    @Override
    public ResponseEntity<Void> updateInternalAccountPassword(UUID accountId, InternalAccountPasswordUpdateRequestDTO internalAccountPasswordUpdateRequestDTO) {
        try {
            accountService.updateInternalAccountPassword(new AccountId(accountId), internalAccountPasswordUpdateRequestDTO.getNewPlaintextPassword());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (WrongProviderException e1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e1.getMessage());
        } catch (AccountNotFoundByIdException e2) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e2.getMessage());
        } catch (InvalidPasswordException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (ConstraintViolationException e4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please enter valid inputs.");
        }
    }


    @Override
    public ResponseEntity<InternalAccountDTO> updateInternalAccountEmail(UUID accountId, InternalAccountEmailUpdateRequestDTO internalAccountEmailUpdateRequestDTO) {
        try {
            InternalAccount internalAccount = accountService.updateInternalAccountEmail(new AccountId(accountId), internalAccountEmailUpdateRequestDTO.getNewEmail());
            return ResponseEntity.ok(accountDtoMapper.toInternalDTO(internalAccount));
        } catch (EmailAlreadyInUserException | WrongProviderException e1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e1.getMessage());
        } catch (AccountNotFoundByIdException | InvalidEmailException e2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e2.getMessage());
        } catch (ConstraintViolationException e4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please enter valid inputs.");
        }
    }

    @Override
    public ResponseEntity<AccountLoginResponseDTO> loginInternalAccount() {
        InternalAccountDetails accountDetails = (InternalAccountDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        InternalAccount internalAccount = accountDetails.internalAccount();
        AccessToken accountAccessToken = accountService.mintAccountAccessToken(internalAccount);
        Jwt accountJwt = new Jwt(
                accountAccessToken.getTokenValue(),
                accountAccessToken.getIssuedAt(),
                accountAccessToken.getExpiresAt(),
                accountAccessToken.getHeaders(),
                accountAccessToken.getClaims()
        );
        InternalAccountDTO internalAccountDTO = accountDtoMapper.toInternalDTO(internalAccount);
        return ResponseEntity.ok(accountDtoMapper.toLoginResponseDTO(internalAccountDTO, accountJwt));
    }

    @Override
    public ResponseEntity<AccountLoginResponseDTO> loginOIDCAccount() {
        AuthenticatedOidcIdToken authenticatedOidcIdToken = (AuthenticatedOidcIdToken) SecurityContextHolder.getContext().getAuthentication();
        OidcAccount oidcAccount = (OidcAccount) authenticatedOidcIdToken.getOidcAccount();
        if (oidcAccount == null) { // No account exists yet
            try {
                oidcAccount = accountService.registerNewOidcAccount(authenticatedOidcIdToken.getSubject());
            } catch (AccountAlreadyExistsException e) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
            }
        }
        AccessToken accountAccessToken = accountService.mintAccountAccessToken(oidcAccount);
        Jwt accountJwt = new Jwt(
                accountAccessToken.getTokenValue(),
                accountAccessToken.getIssuedAt(),
                accountAccessToken.getExpiresAt(),
                accountAccessToken.getHeaders(),
                accountAccessToken.getClaims()
        );
        OidcAccountDTO oidcAccountDTO = accountDtoMapper.toOidcDTO(oidcAccount);
        return ResponseEntity.ok(accountDtoMapper.toLoginResponseDTO(oidcAccountDTO, accountJwt));
    }

    @Override
    public ResponseEntity<Void> deleteAccount(UUID accountId) {
        try {
            accountService.deleteAccount(new AccountId(accountId));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (AccountNotFoundByIdException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AccountStillVerifiedException e2) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e2.getMessage());
        }
    }

}
