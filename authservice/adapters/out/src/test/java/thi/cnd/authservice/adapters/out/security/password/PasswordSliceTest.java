package thi.cnd.authservice.adapters.out.security.password;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import thi.cnd.authservice.domain.exceptions.InvalidPasswordException;
import thi.cnd.authservice.domain.model.AccessToken;
import thi.cnd.authservice.domain.model.account.Account;
import thi.cnd.authservice.domain.model.account.AccountId;
import thi.cnd.authservice.domain.model.account.InternalAccount;

import java.time.Instant;
import java.util.Base64;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {PasswordProviderImpl.class, PassayPasswordGenerator.class, PasswordValidator.class})
public class PasswordSliceTest {

    @Autowired
    PasswordProviderImpl passwordProvider;

    @Autowired
    PassayPasswordGenerator passwordGenerator;

    @Autowired
    PasswordValidator passwordValidator;

    @Test
    public void insecurePasswordTest (){
        String insufficientPassword = "insufficent_password";
        assertThrows(InvalidPasswordException.class, () -> passwordProvider.validatePasswordAndEncode(insufficientPassword));
    }

    @Test
    public void securePasswordTest () throws InvalidPasswordException {
        String securePassword = "P@ssw0rd";
        String encodedPassword = passwordProvider.validatePasswordAndEncode(securePassword);
        assertNotEquals(encodedPassword, securePassword);
    }

}
