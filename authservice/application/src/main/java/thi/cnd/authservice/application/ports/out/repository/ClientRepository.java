package thi.cnd.authservice.application.ports.out.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import thi.cnd.authservice.domain.exceptions.ClientAlreadyExistsException;
import thi.cnd.authservice.domain.exceptions.ClientNotFoundByNameException;
import thi.cnd.authservice.domain.model.client.Client;

@Validated
public interface ClientRepository {
    Client findByName(@NotBlank String name) throws ClientNotFoundByNameException;

    Client findByNameAndUpdateLastLogin(@NotBlank String name) throws ClientNotFoundByNameException;

    Client register(@NotNull Client client) throws ClientAlreadyExistsException;

    @NotNull Client save(@NotNull Client client) throws ClientAlreadyExistsException;

    @NotNull Client updatePassword(@NotBlank String name, @NotBlank String encryptedPassword) throws ClientNotFoundByNameException;

    void delete(@NotNull String clientId) throws ClientNotFoundByNameException;
}
