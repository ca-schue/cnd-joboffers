package thi.cnd.authservice.primary.rest.account;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import thi.cnd.authservice.api.generated.AccountManagementApi;
import thi.cnd.authservice.api.generated.model.*;
import thi.cnd.authservice.core.exceptions.*;
import thi.cnd.authservice.core.model.account.*;
import thi.cnd.authservice.core.ports.primary.AccountServicePort;
import thi.cnd.authservice.primary.security.authentication.loginAuthentication.internalAccount.InternalAccountDetails;
import thi.cnd.authservice.primary.security.authentication.loginAuthentication.oidcAccount.AuthenticatedOidcIdToken;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class AccountController implements AccountManagementApi {

    private final AccountServicePort accountServicePort;
    private final AccountLoginApiMapper accountLoginApiMapper;

    @Override
    public ResponseEntity<AccountLoginResponseDTO> registerInternalAccount(InternalAccountRegistrationRequestDTO requestDTO) {
        try {
            InternalAccount registeredInternalAccount = accountServicePort.registerNewInternalAccount(requestDTO.getEmail(), requestDTO.getPassword());
            AccountAccessToken accessToken = accountServicePort.mintAccountAccessToken(registeredInternalAccount);
            InternalAccountDTO internalAccountDTO = accountLoginApiMapper.toInternalDTO(registeredInternalAccount);
            return ResponseEntity.ok(accountLoginApiMapper.toLoginResponseDTO(internalAccountDTO, accessToken));
        } catch (AccountAlreadyExistsException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        } catch (InvalidPasswordException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Void> updateInternalAccountPassword(UUID accountId, InternalAccountPasswordUpdateRequestDTO internalAccountPasswordUpdateRequestDTO) {
        try {
            accountServicePort.updateInternalAccountPassword(new AccountId(accountId), internalAccountPasswordUpdateRequestDTO.getNewPlaintextPassword());
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
            InternalAccount internalAccount = accountServicePort.updateInternalAccountEmail(new AccountId(accountId), internalAccountEmailUpdateRequestDTO.getNewEmail());
            return ResponseEntity.ok(accountLoginApiMapper.toInternalDTO(internalAccount));
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
        AccountAccessToken accessToken = accountServicePort.mintAccountAccessToken(internalAccount);
        InternalAccountDTO internalAccountDTO = accountLoginApiMapper.toInternalDTO(internalAccount);
        return ResponseEntity.ok(accountLoginApiMapper.toLoginResponseDTO(internalAccountDTO, accessToken));
    }

    @Override
    public ResponseEntity<AccountLoginResponseDTO> loginOIDCAccount() {
        AuthenticatedOidcIdToken authenticatedOidcIdToken = (AuthenticatedOidcIdToken) SecurityContextHolder.getContext().getAuthentication();
        OidcAccount oidcAccount = (OidcAccount) authenticatedOidcIdToken.getPrincipal();
        if (oidcAccount == null) { // No account exists yet
            try {
                oidcAccount = accountServicePort.registerNewOidcAccount(authenticatedOidcIdToken.getSubject());
            } catch (AccountAlreadyExistsException e) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
            }
        }
        AccountAccessToken accessToken = accountServicePort.mintAccountAccessToken(oidcAccount);
        OidcAccountDTO oidcAccountDTO = accountLoginApiMapper.toOidcDTO(oidcAccount);
        return ResponseEntity.ok(accountLoginApiMapper.toLoginResponseDTO(oidcAccountDTO, accessToken));
    }

    @Override
    public ResponseEntity<Void> deleteAccount(UUID accountId) {
        try {
            accountServicePort.deleteAccount(new AccountId(accountId));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (AccountNotFoundByIdException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AccountStillVerifiedException e2) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e2.getMessage());
        }
    }

}
