package thi.cnd.userservice.adapters.in.http.account;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import thi.cnd.authservice.api.generated.AccountManagementApi;
import thi.cnd.authservice.api.generated.model.*;
import thi.cnd.authservice.domain.AccountService;
import thi.cnd.authservice.domain.exceptions.*;
import thi.cnd.authservice.domain.model.account.*;
import thi.cnd.userservice.adapters.in.security.authentication.loginAuthentication.internalAccount.InternalAccountDetails;
import thi.cnd.userservice.adapters.in.security.authentication.loginAuthentication.oidcAccount.AuthenticatedOidcIdToken;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class AccountHttpControllerImpl implements AccountManagementApi {

    private final AccountService accountService;
    private final AccountDtoMapper accountDtoMapper;

    @Override
    public ResponseEntity<AccountLoginResponseDTO> registerInternalAccount(InternalAccountRegistrationRequestDTO requestDTO) {
        try {
            InternalAccount registeredInternalAccount = accountService.registerNewInternalAccount(requestDTO.getEmail(), requestDTO.getPassword());
            AccountAccessToken accessToken = accountService.mintAccountAccessToken(registeredInternalAccount);
            InternalAccountDTO internalAccountDTO = accountDtoMapper.toInternalDTO(registeredInternalAccount);
            return ResponseEntity.ok(accountDtoMapper.toLoginResponseDTO(internalAccountDTO, accessToken));
        } catch (AccountAlreadyExistsException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        } catch (InvalidPasswordException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
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
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
    }


    @Override
    public ResponseEntity<InternalAccountDTO> updateInternalAccountEmail(UUID accountId, InternalAccountEmailUpdateRequestDTO internalAccountEmailUpdateRequestDTO) {
        try {
            InternalAccount internalAccount = accountService.updateInternalAccountEmail(new AccountId(accountId), internalAccountEmailUpdateRequestDTO.getNewEmail());
            return ResponseEntity.ok(accountDtoMapper.toInternalDTO(internalAccount));
        } catch (EmailAlreadyInUserException | WrongProviderException e1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e1.getMessage());
        } catch (AccountNotFoundByIdException e2) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e2.getMessage());
        }
    }

    @Override
    public ResponseEntity<AccountLoginResponseDTO> loginInternalAccount() {
        InternalAccountDetails accountDetails = (InternalAccountDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        InternalAccount internalAccount = accountDetails.internalAccount();
        AccountAccessToken accessToken = accountService.mintAccountAccessToken(internalAccount);
        InternalAccountDTO internalAccountDTO = accountDtoMapper.toInternalDTO(internalAccount);
        return ResponseEntity.ok(accountDtoMapper.toLoginResponseDTO(internalAccountDTO, accessToken));
    }

    @Override
    public ResponseEntity<AccountLoginResponseDTO> loginOIDCAccount() {
        AuthenticatedOidcIdToken authenticatedOidcIdToken = (AuthenticatedOidcIdToken) SecurityContextHolder.getContext().getAuthentication();
        OidcAccount oidcAccount = (OidcAccount) authenticatedOidcIdToken.getPrincipal();
        if (oidcAccount == null) { // No account exists yet
            try {
                oidcAccount = accountService.registerNewOidcAccount(authenticatedOidcIdToken.getSubject());
            } catch (AccountAlreadyExistsException e) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
            }
        }
        AccountAccessToken accessToken = accountService.mintAccountAccessToken(oidcAccount);
        OidcAccountDTO oidcAccountDTO = accountDtoMapper.toOidcDTO(oidcAccount);
        return ResponseEntity.ok(accountDtoMapper.toLoginResponseDTO(oidcAccountDTO, accessToken));
    }

    @Override
    public ResponseEntity<Void> deleteAccount(UUID accountId) {
        try {
            accountService.deleteAccount(new AccountId(accountId));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (AccountNotFoundByIdException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AccountStillVerifiedException e2) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e2.getMessage());
        }
    }

}
