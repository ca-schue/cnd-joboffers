package thi.cnd.userservice.adapters.in.http.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import thi.cnd.authservice.api.generated.AuthEndpointsApi;
import thi.cnd.authservice.api.generated.model.AccountDTO;
import thi.cnd.authservice.api.generated.model.ClientDTO;
import thi.cnd.authservice.application.ports.out.repository.AccountRepositoryPort;
import thi.cnd.authservice.application.ports.out.repository.ClientRepositoryPort;
import thi.cnd.authservice.domain.exceptions.*;
import thi.cnd.authservice.domain.model.account.*;
import thi.cnd.authservice.domain.model.client.*;
import thi.cnd.userservice.adapters.in.http.account.AccountDtoMapper;
import thi.cnd.userservice.adapters.in.security.authentication.accessTokenAuthentication.AuthenticatedAccount;
import thi.cnd.userservice.adapters.in.security.authentication.accessTokenAuthentication.AuthenticatedClient;

@RestController
@AllArgsConstructor
public class AuthController implements AuthEndpointsApi {

    private final ClientRepositoryPort clientRepositoryPort;
    private final AccountRepositoryPort accountRepositoryPort;
    private final AccountDtoMapper accountDtoMapper;


    @Override
    public ResponseEntity<AccountDTO> tokenIntrospectionAccount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AuthenticatedAccount authAcc) {
            try {
                AccountId accountId = authAcc.getAccountId();
                Account account = accountRepositoryPort.findAccountById(accountId);
                switch (account) {
                    case InternalAccount ia -> { return ResponseEntity.ok(accountDtoMapper.toInternalDTO(ia)); }
                    case OidcAccount oa -> { return ResponseEntity.ok(accountDtoMapper.toOidcDTO(oa)); }
                    default -> throw new AccountNotFoundByIdException("Unknown account type: Neither internal nor oidc account.");
                }
            } catch (AccountNotFoundByIdException e) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "TODO");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Endpoint does not support given JWT");
        }
    }

    @Override
    public ResponseEntity<ClientDTO> tokenIntrospectionClient() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AuthenticatedClient authClient ) {
            String clientName = authClient.getClientName();
            try {
                Client client = clientRepositoryPort.findByName(clientName);
                return ResponseEntity.ok(new ClientDTO("CLIENT", client.name(), client.audiences()));
            } catch (ClientNotFoundByNameException e) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "TODO");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Endpoint does not support given JWT");
        }
    }
}
