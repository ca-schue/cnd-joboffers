package thi.cnd.authservice.core.domain;

import lombok.RequiredArgsConstructor;
import org.ietf.jgss.Oid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import thi.cnd.authservice.core.domain.password.PasswordValidator;
import thi.cnd.authservice.core.exceptions.InvalidPasswordException;
import thi.cnd.authservice.core.model.account.*;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class AccountFactory {

    private final PasswordEncoder passwordEncoder;
    private final PasswordValidator passwordValidator;

    public OidcAccount buildOidc(String subject) {
        return new OidcAccount(
                new AccountId(),
                Instant.now(),
                subject,
                false
        );
    }

    public InternalAccount buildInternal(String email, String password) throws InvalidPasswordException {
            return new InternalAccount(
                    new AccountId(),
                    Instant.now(),
                    email,
                    validatePasswordAndEncode(password),
                    false
                    );
    }

    public InternalAccount updatePassword(InternalAccount internalAccount, String newPlainTextPassword) throws InvalidPasswordException {
        return new InternalAccount(
                internalAccount.getId(),
                internalAccount.getLastLogin(),
                internalAccount.getEmail(),
                validatePasswordAndEncode(newPlainTextPassword),
                internalAccount.isVerified()
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
