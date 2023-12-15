package thi.cnd.authservice.adapters.out.security.password;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import thi.cnd.authservice.domain.exceptions.InvalidPasswordException;
import thi.cnd.authservice.domain.model.AccessToken;
import thi.cnd.authservice.domain.model.account.Account;
import thi.cnd.authservice.domain.model.account.AccountId;
import thi.cnd.authservice.domain.model.account.InternalAccount;

import java.time.Instant;
import java.util.Base64;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {PasswordProviderImpl.class, PassayPasswordGenerator.class, PasswordValidator.class})
public class PasswordSliceTest {

    @Autowired
    PasswordProviderImpl passwordProvider;

    @Autowired
    PassayPasswordGenerator passwordGenerator;

    @Autowired
    PasswordValidator passwordValidator;

    @Test
    public void passwordTests (){
        String insufficientPassword = "insufficent_password";
        assertThrows(InvalidPasswordException.class, () -> passwordProvider.validatePasswordAndEncode(insufficientPassword));
    }

}
