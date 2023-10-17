package thi.cnd.authservice.core.ports.secondary;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import thi.cnd.authservice.core.exceptions.ClientAlreadyExistsException;
import thi.cnd.authservice.core.exceptions.ClientNotFoundByNameException;
import thi.cnd.authservice.core.model.Client;

import java.util.Optional;

@Validated
public interface ClientRepositoryPort {
    Client findByName(@NotBlank String name) throws ClientNotFoundByNameException;

    Client findByNameAndUpdateLastLogin(@NotBlank String name) throws ClientNotFoundByNameException;

    Client register(@NotNull Client client) throws ClientAlreadyExistsException;

    @NotNull Client save(@NotNull Client client) throws ClientAlreadyExistsException;

    @NotNull Client updatePassword(@NotBlank String name, @NotBlank String encryptedPassword) throws ClientNotFoundByNameException;
}
