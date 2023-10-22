package thi.cnd.authservice.primary.rest.account;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import thi.cnd.authservice.api.generated.AccountLoginApi;
import thi.cnd.authservice.api.generated.AccountManagementApi;
import thi.cnd.authservice.api.generated.model.AccessTokenResponseDTO;
import thi.cnd.authservice.api.generated.model.AccountDTO;
import thi.cnd.authservice.api.generated.model.AccountEmailUpdateRequestDTO;
import thi.cnd.authservice.api.generated.model.InternalAccountRegistrationRequestDTO;
import thi.cnd.authservice.core.exceptions.*;
import thi.cnd.authservice.core.model.account.*;
import thi.cnd.authservice.core.ports.primary.AccountServicePort;
import thi.cnd.authservice.primary.security.authentication.loginAuthentication.internalAccount.InternalAccountDetails;
import thi.cnd.authservice.primary.security.authentication.loginAuthentication.oidc.AuthenticatedOidcIdToken;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class AccountController implements AccountLoginApi, AccountManagementApi {

    private final AccountServicePort accountServicePort;
    private final AccountLoginApiMapper accountLoginApiMapper;

    @Override
    public ResponseEntity<AccountDTO> registerInternalAccount(InternalAccountRegistrationRequestDTO requestDTO) {
        try {
            InternalAccount internalAccount = accountServicePort.registerNewInternalAccount(requestDTO.getEmail(), requestDTO.getPassword());
            return ResponseEntity.ok(accountLoginApiMapper.toInternalDTO(internalAccount));
        } catch (AccountAlreadyExistsException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        } catch (InvalidPasswordException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<AccountDTO> updateAccountEmail(UUID accountId, AccountEmailUpdateRequestDTO accountEmailUpdateRequestDTO) {
        try {
            InternalAccount internalAccount = accountServicePort.updateInternalAccountEmail(new AccountId(accountId), accountEmailUpdateRequestDTO.getEmail());
            return ResponseEntity.ok(accountLoginApiMapper.toInternalDTO(internalAccount));
        } catch (EmailAlreadyInUserException e1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e1.getMessage());
        } catch (AccountNotFoundByIdException e2) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e2.getMessage());
        }
    }

    @Override
    public ResponseEntity<AccessTokenResponseDTO> loginInternalAccount() {
        InternalAccountDetails accountDetails = (InternalAccountDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        InternalAccount internalAccount = accountDetails.internalAccount();
        AccountAccessToken accessToken = accountServicePort.mintAccessToken(internalAccount);
        return ResponseEntity.ok(accountLoginApiMapper.toDTO(accessToken));
    }

    @Override
    public ResponseEntity<AccessTokenResponseDTO> loginOIDCAccount() {
        AuthenticatedOidcIdToken authenticatedOidcIdToken = (AuthenticatedOidcIdToken) SecurityContextHolder.getContext().getAuthentication();
        OidcAccount oidcAccount = (OidcAccount) authenticatedOidcIdToken.getPrincipal();
        if (oidcAccount == null) { // No account exists yet
            try {
                oidcAccount = accountServicePort.registerNewOidcAccount(authenticatedOidcIdToken.getSubject());
            } catch (AccountAlreadyExistsException e) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
            }
        }
        String email = authenticatedOidcIdToken.getEmail(); // extract email from Authenticated object / ID-Token
        AccountAccessToken accessToken = accountServicePort.mintAccessToken(oidcAccount);
        return ResponseEntity.ok(accountLoginApiMapper.toDTO(accessToken));
    }

    @Override
    public ResponseEntity<Void> deleteAccount(UUID accountId) {
        try {
            accountServicePort.deleteAccount(new AccountId(accountId));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (AccountNotFoundByIdException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
