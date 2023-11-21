package thi.cnd.authservice.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import thi.cnd.authservice.domain.exceptions.ClientAlreadyExistsException;
import thi.cnd.authservice.domain.exceptions.ClientNotFoundByNameException;
import thi.cnd.authservice.domain.model.client.Client;
import thi.cnd.authservice.domain.model.client.ClientAccessToken;
import thi.cnd.authservice.domain.model.client.ClientWithPlaintextPassword;

import java.util.Set;

public interface ClientService {
    @NotNull
    ClientWithPlaintextPassword createNewClient(@NotBlank String name, @NotNull Set<String> audiences, Set<String> scopes) throws ClientAlreadyExistsException;

    @NotNull ClientAccessToken mintClientAccessToken(Client client);

    @NotNull
    ClientWithPlaintextPassword setNewRandomPassword(@NotBlank String name) throws ClientNotFoundByNameException;

    void deleteClient(String name) throws ClientNotFoundByNameException;
}
