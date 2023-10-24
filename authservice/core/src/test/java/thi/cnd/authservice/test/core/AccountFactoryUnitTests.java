package thi.cnd.authservice.test.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import thi.cnd.authservice.core.domain.AccountFactory;
import thi.cnd.authservice.core.domain.password.PasswordEncoder;
import thi.cnd.authservice.core.domain.password.PasswordValidator;
import thi.cnd.authservice.core.exceptions.AccountAlreadyExistsException;
import thi.cnd.authservice.core.exceptions.InvalidPasswordException;
import thi.cnd.authservice.core.model.account.AccountProvider;
import thi.cnd.authservice.core.model.account.InternalAccount;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AccountFactoryUnitTests {

    private static long LAST_LOGIN_SEC_TOLERANCE = 2;

    private AccountFactory accountFactory;

    private static PasswordEncoder passwordEncoder;
    private static PasswordValidator passwordValidator;

    @BeforeAll
    static void setupPasswordEncoderAndValidator() {
        passwordEncoder = new PasswordEncoder();
        passwordValidator = new PasswordValidator();
    }

    @BeforeEach
    void initUseCase() {
        accountFactory = new AccountFactory(passwordEncoder, passwordValidator);
    }

    @Test
    void accountFactoryTest_InvalidPassword() {
        String email = "test_email";
        String invalidPassword = "insecure_password";
        Assertions.assertThrows(InvalidPasswordException.class,
                () -> {
                    accountFactory.buildInternal(email, invalidPassword);
                });
    }

    @Test
    void accountFactoryTest_ValidPassword() throws InvalidPasswordException {
        String email = "test_email";
        String validPasswordPlaintext = "A1b2_c3?d4";
        InternalAccount internalAccount = accountFactory.buildInternal(email, validPasswordPlaintext);
        assertThat(internalAccount.getId().id()).isNotNull();
        assertThat(internalAccount.getProvider()).isEqualTo(AccountProvider.INTERNAL);
        assertThat(internalAccount.getLastLogin()).isBeforeOrEqualTo(Instant.now()).isAfterOrEqualTo(Instant.now().minusSeconds(LAST_LOGIN_SEC_TOLERANCE));
        assertThat(internalAccount.getEmail()).isEqualTo(email);
        assertThat(internalAccount.getEncryptedPassword()).isNotEqualTo(validPasswordPlaintext);
    }

}

