package thi.cnd.authservice.adapters.out.repository.accounts.DAOs;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;
import thi.cnd.authservice.domain.model.account.AccountId;

import java.time.Instant;

@Getter
@Setter
@Validated
@Document(collection = "Accounts")
public abstract class AccountDAO {
        @Id
        private AccountId id;

        @Indexed
        private String provider;

        @Indexed
        private Instant lastLogin;

        @NotNull
        private boolean verified;
}

