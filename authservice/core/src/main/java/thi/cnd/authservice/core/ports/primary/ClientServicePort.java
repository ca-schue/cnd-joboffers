package thi.cnd.authservice.core.ports.primary;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import thi.cnd.authservice.core.domain.ClientService;
import thi.cnd.authservice.core.exceptions.ClientAlreadyExistsException;
import thi.cnd.authservice.core.exceptions.ClientNotFoundByNameException;
import thi.cnd.authservice.core.model.client.Client;
import thi.cnd.authservice.core.model.client.ClientAccessToken;
import thi.cnd.authservice.core.model.client.ClientWithPlaintextPassword;

import java.util.Set;

public interface ClientServicePort {
    @NotNull
    ClientWithPlaintextPassword createNewClient(@NotBlank String name, @NotNull Set<String> audiences, Set<String> scopes) throws ClientAlreadyExistsException;

    @NotNull ClientAccessToken mintClientAccessToken(Client client);

    @NotNull
    ClientWithPlaintextPassword setNewRandomPassword(@NotBlank String name) throws ClientNotFoundByNameException;

    void deleteClient(String name) throws ClientNotFoundByNameException;
}
