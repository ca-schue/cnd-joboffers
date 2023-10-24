package thi.cnd.authservice.test.core.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

@ExtendWith(MockitoExtension.class)
class AccountServiceIntegrationTest {

    // TODO: loginInternalAccount

}

