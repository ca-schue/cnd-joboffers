package thi.cnd.authservice.domain.model.client;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Set;

public record Client (
        @NotNull String name,
        @NotEmpty String encryptedPassword,
        @NotNull Set<String> audiences,
        Set<String> scopes,
        @NotNull Instant lastLogin,
        @NotNull Instant lastPasswordChange
){
    public Client {
        if (name.contains(" ")) {
            throw new RuntimeException("Client name cannot contain whitespaces");
        }
    }
}
