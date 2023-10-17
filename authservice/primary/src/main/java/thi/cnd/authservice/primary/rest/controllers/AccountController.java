package thi.cnd.authservice.primary.rest.controllers;

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
import thi.cnd.authservice.api.generated.model.InternalAccountLoginRequestDTO;
import thi.cnd.authservice.api.generated.model.InternalAccountRegistrationRequestDTO;
import thi.cnd.authservice.core.exceptions.AccountAlreadyExistsException;
import thi.cnd.authservice.core.exceptions.AccountNotFoundByEmailException;
import thi.cnd.authservice.core.exceptions.AccountNotFoundByIdException;
import thi.cnd.authservice.core.exceptions.WrongProviderException;
import thi.cnd.authservice.core.model.Account;
import thi.cnd.authservice.core.model.AccountAccessToken;
import thi.cnd.authservice.core.model.AccountId;
import thi.cnd.authservice.core.ports.primary.AccountServicePort;
import thi.cnd.authservice.primary.rest.model.AccountLoginApiMapper;
import thi.cnd.authservice.primary.rest.security.AuthenticatedOidcIdToken;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class AccountController implements AccountLoginApi, AccountManagementApi {

    private final AccountServicePort accountServicePort;
    private final AccountLoginApiMapper accountLoginApiMapper;

    @Override
    public ResponseEntity<AccountDTO> registerInternalAccount(InternalAccountRegistrationRequestDTO requestDTO) {
        Account account;
        try {
            account = accountServicePort.registerNewInternalAccount(requestDTO.getEmail(), requestDTO.getPassword());
        } catch (AccountAlreadyExistsException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        }
        return ResponseEntity.ok(accountLoginApiMapper.toDTO(account));
    }

    @Override
    public ResponseEntity<AccessTokenResponseDTO> loginInternalAccount(InternalAccountLoginRequestDTO requestDTO) {
        try {
            AccountAccessToken accessToken = accountServicePort.mintAccessTokenInternalProvider(requestDTO.getEmail());
            return ResponseEntity.ok(accountLoginApiMapper.toDTO(accessToken));
        } catch (AccountNotFoundByEmailException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (WrongProviderException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<AccessTokenResponseDTO> loginOIDCAccount() {
        AuthenticatedOidcIdToken authenticatedOidcIdToken = (AuthenticatedOidcIdToken) SecurityContextHolder.getContext().getAuthentication();
        String email = authenticatedOidcIdToken.getEmail(); // extract email from Authenticated object / ID-Token
        try {
            AccountAccessToken accessToken = accountServicePort.mintAccessTokenOidcProvider(email);
            return ResponseEntity.ok(accountLoginApiMapper.toDTO(accessToken));
        } catch (WrongProviderException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
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
