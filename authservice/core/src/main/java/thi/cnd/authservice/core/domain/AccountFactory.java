package thi.cnd.authservice.core.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import thi.cnd.authservice.core.exceptions.InvalidPasswordException;
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
                "", // No PW for OIDC Account
                Instant.now()
        );
    }

    public Account buildInternal(String email, String password) throws InvalidPasswordException {
            return new Account(
                    new AccountId(),
                    AccountProvider.INTERNAL,
                    email,
                    validatePasswordAndEncode(password),
                    Instant.now()
                    );
    }

    private String validatePasswordAndEncode(String cleartextPassword) throws InvalidPasswordException {
        if (passwordValidator.isCleartextPasswordValid(cleartextPassword)) {
            return passwordEncoder.encode(cleartextPassword);
        } else {
            throw new InvalidPasswordException("Password must fulfill security requirements.");
        }
    }

}
