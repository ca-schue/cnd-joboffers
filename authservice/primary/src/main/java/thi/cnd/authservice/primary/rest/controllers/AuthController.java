package thi.cnd.authservice.primary.rest.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import thi.cnd.authservice.api.generated.AuthEndpointsApi;
import thi.cnd.authservice.api.generated.model.AccountDTO;
import thi.cnd.authservice.api.generated.model.AuthenticatedSubjectResponseDTO;
import thi.cnd.authservice.api.generated.model.ClientDTO;
import thi.cnd.authservice.api.generated.model.InternalAccountDTO;
import thi.cnd.authservice.core.exceptions.AccountNotFoundByIdException;
import thi.cnd.authservice.core.exceptions.ClientNotFoundByNameException;
import thi.cnd.authservice.core.model.account.Account;
import thi.cnd.authservice.core.model.account.AccountId;
import thi.cnd.authservice.core.model.account.InternalAccount;
import thi.cnd.authservice.core.model.account.OidcAccount;
import thi.cnd.authservice.core.model.client.Client;
import thi.cnd.authservice.core.ports.secondary.AccountRepositoryPort;
import thi.cnd.authservice.core.ports.secondary.ClientRepositoryPort;
import thi.cnd.authservice.primary.rest.account.AccountLoginApiMapper;
import thi.cnd.authservice.primary.security.authentication.accessTokenAuthentication.AuthenticatedAccount;
import thi.cnd.authservice.primary.security.authentication.accessTokenAuthentication.AuthenticatedClient;

@RestController
@AllArgsConstructor
public class AuthController implements AuthEndpointsApi {

    private final ClientRepositoryPort clientRepositoryPort;
    private final AccountRepositoryPort accountRepositoryPort;
    private final AccountLoginApiMapper accountLoginApiMapper;

    @Override
    public ResponseEntity<AuthenticatedSubjectResponseDTO> tokenIntrospection() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return switch (auth) {
            case AuthenticatedClient authClient -> {
                String clientName = authClient.getClientName();
                try {
                    Client client = clientRepositoryPort.findByName(clientName);
                    yield ResponseEntity.ok(new ClientDTO("CLIENT", client.name(), client.audiences()));
                } catch (ClientNotFoundByNameException e) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "TODO");
                }
            }
            case AuthenticatedAccount authAcc -> {
                try {
                    AccountId accountId = authAcc.getAccountId();
                    Account account = accountRepositoryPort.findAccountById(accountId);
                    switch (account) {
                        case InternalAccount ia -> { yield ResponseEntity.ok(accountLoginApiMapper.toInternalDTO(ia)); }
                        case OidcAccount oa -> { yield ResponseEntity.ok(accountLoginApiMapper.toOidcDTO(oa)); }
                        default -> throw new AccountNotFoundByIdException("Unknown account type: Neither internal nor oidc account.");
                    }
                } catch (AccountNotFoundByIdException e) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "TODO");
                }
            }
            default -> {
                // TODO!
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated with valid JWT");
            }
        };
    }
}
