package thi.cnd.authservice.core.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import thi.cnd.authservice.core.model.Account;
import thi.cnd.authservice.core.model.AccountId;
import thi.cnd.authservice.core.model.AccountProvider;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class AccountFactory {

    private final PasswordEncoder passwordEncoder;
    private final PasswordValidator passwordValidator;

    public Account buildOidc(String email) {
        return new Account(
                new AccountId(),
                AccountProvider.OIDC,
                email,
                "",
                Instant.now()
        );
    }

    public Account buildInternal(String email, String password) {
            return new Account(
                    new AccountId(),
                    AccountProvider.INTERNAL,
                    email,
                    validatePasswordAndEncode(password),
                    Instant.now()
                    );
    }

    private String validatePasswordAndEncode(String cleartextPassword) {
        if (passwordValidator.isCleartextPasswordValid(cleartextPassword)) {
            return passwordEncoder.encode(cleartextPassword);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must fulfill security requirements.");
        }
    }

}
