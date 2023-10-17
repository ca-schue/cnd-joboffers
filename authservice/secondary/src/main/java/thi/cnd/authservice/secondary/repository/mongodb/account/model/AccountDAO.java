package thi.cnd.authservice.secondary.repository.mongodb.account.model;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;
import thi.cnd.authservice.core.model.AccountId;

import java.time.Instant;

@Validated
@Document(collection = "Accounts")
public record AccountDAO(
        @Id
        AccountId id,

        @Indexed
        String provider,

        @Indexed(unique = true)
        String email,

        String encryptedPassword,
        @Indexed
        Instant lastLogin
) { }

