package thi.cnd.authservice.core.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.Instant;


public record Account (
        @NotNull AccountId id,
        @NotNull AccountProvider provider,
        @NotBlank String email,
        String encryptedPassword, // can be blank (better: seperate two classes Internal/OidcAccount with and without PW)
        @NotNull Instant lastLogin
)
{}
