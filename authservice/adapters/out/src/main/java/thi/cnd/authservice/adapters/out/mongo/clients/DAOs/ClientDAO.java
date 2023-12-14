package thi.cnd.authservice.adapters.out.mongo.clients.DAOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.util.Set;

@Validated
@Document(collection = "Clients")
public record ClientDAO(
        @Id String name,
        @NotBlank String encryptedPassword,
        @NotEmpty Set<String> audiences,
        Set<String> scopes,
        @Indexed(background = true) @NotNull Instant lastLogin,
        @Indexed(background = true) @NotNull Instant lastPasswordChange
) {
}
