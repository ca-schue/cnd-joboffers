package thi.cnd.authservice.test.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import thi.cnd.authservice.core.domain.AccountFactory;
import thi.cnd.authservice.core.domain.AccountService;
import thi.cnd.authservice.core.domain.jwt.JwtProvider;
import thi.cnd.authservice.core.domain.password.PasswordEncoder;
import thi.cnd.authservice.core.domain.password.PasswordValidator;
import thi.cnd.authservice.core.exceptions.AccountAlreadyExistsException;
import thi.cnd.authservice.core.exceptions.InvalidPasswordException;
import thi.cnd.authservice.core.model.account.AccountProvider;
import thi.cnd.authservice.core.model.account.InternalAccount;
import thi.cnd.authservice.core.ports.primary.AccountServicePort;
import thi.cnd.authservice.core.ports.secondary.AccountRepositoryPort;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    private static long LAST_LOGIN_SEC_TOLERANCE = 2;

    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private AccountRepositoryPort accountRepositoryPort;

    private static AccountFactory accountFactory;

    private static PasswordEncoder passwordEncoder;
    private static PasswordValidator passwordValidator;

    private AccountServicePort accountServicePort;

    @BeforeAll
    static void setupUnitTests() {
        passwordEncoder = new PasswordEncoder();
        passwordValidator = new PasswordValidator();
        accountFactory = new AccountFactory(passwordEncoder, passwordValidator);
    }

    @BeforeEach
    void initUseCase() {
        accountServicePort = new AccountService(jwtProvider, accountRepositoryPort, accountFactory);
    }

    @Test
    void accountFactoryTest_InvalidPassword() throws AccountAlreadyExistsException {
        String email = "test_email";
        String invalidPassword = "insecure_password";
        Assertions.assertThrows(InvalidPasswordException.class,
                () -> {
                    accountServicePort.registerNewInternalAccount(email, invalidPassword);
                });
    }

    // TODO: Passwordencoder Unit Tests

    @Test
    void accountFactoryTest_ValidPassword() throws AccountAlreadyExistsException, InvalidPasswordException {
        String email = "test_email";
        String validPasswordPlaintext = "A1b2_c3?d4";
        InternalAccount internalAccount = accountServicePort.registerNewInternalAccount(email, validPasswordPlaintext);
        assertThat(internalAccount.getId().id()).isNotNull();
        assertThat(internalAccount.getProvider()).isEqualTo(AccountProvider.INTERNAL);
        assertThat(internalAccount.getLastLogin()).isBeforeOrEqualTo(Instant.now()).isAfterOrEqualTo(Instant.now().minusSeconds(LAST_LOGIN_SEC_TOLERANCE));
        assertThat(internalAccount.getEmail()).isEqualTo(email);
        assertThat(internalAccount.getEncryptedPassword()).isNotEqualTo(validPasswordPlaintext);
    }

}

