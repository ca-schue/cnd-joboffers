package thi.cnd.authservice.core.ports.primary;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import thi.cnd.authservice.core.exceptions.ClientAlreadyExistsException;
import thi.cnd.authservice.core.exceptions.ClientNotFoundByNameException;
import thi.cnd.authservice.core.model.ClientWithPlaintextPassword;

import java.util.Set;

public interface ClientServicePort {
    @NotNull
    ClientWithPlaintextPassword createNewClient(@NotBlank String name, @NotNull Set<String> audiences, Set<String> scopes) throws ClientAlreadyExistsException;

    @NotNull
    ClientWithPlaintextPassword setNewRandomPassword(@NotBlank String name) throws ClientNotFoundByNameException;

}
