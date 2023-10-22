package thi.cnd.authservice.secondary.repository.account.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;
import thi.cnd.authservice.core.model.account.AccountId;

import java.time.Instant;

@AllArgsConstructor
@Getter
@Setter
@Validated
@Document(collection = "Accounts")
public class AccountDAO {
        @Id
        private AccountId id;

        @Indexed
        private String provider;

        @Indexed
        private Instant lastLogin;
}

