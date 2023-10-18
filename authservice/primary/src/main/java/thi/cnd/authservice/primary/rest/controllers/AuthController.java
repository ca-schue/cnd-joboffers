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
import thi.cnd.authservice.core.domain.ClientService;
import thi.cnd.authservice.core.exceptions.AccountNotFoundByIdException;
import thi.cnd.authservice.core.exceptions.ClientNotFoundByNameException;
import thi.cnd.authservice.core.model.Account;
import thi.cnd.authservice.core.model.AccountId;
import thi.cnd.authservice.core.model.Client;
import thi.cnd.authservice.core.ports.secondary.AccountRepositoryPort;
import thi.cnd.authservice.core.ports.secondary.ClientRepositoryPort;
import thi.cnd.authservice.primary.rest.security.AuthenticatedAccount;
import thi.cnd.authservice.primary.rest.security.AuthenticatedClient;

@RestController
@AllArgsConstructor
public class AuthController implements AuthEndpointsApi {

    private final ClientRepositoryPort clientRepositoryPort;
    private final AccountRepositoryPort accountRepositoryPort;

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
                    yield ResponseEntity.ok(new AccountDTO("ACCOUNT", account.provider().name(), accountId.id(), account.email()));
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
