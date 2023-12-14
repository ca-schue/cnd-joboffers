package thi.cnd.authservice.adapters.out.security.password;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import thi.cnd.authservice.application.ports.out.security.PasswordProvider;
import thi.cnd.authservice.domain.exceptions.InvalidPasswordException;

@Component
public class PasswordProviderImpl implements PasswordProvider {

    PassayPasswordGenerator passayPasswordGenerator;
    BCryptPasswordEncoder bCryptPasswordEncoder;
    PasswordValidator passwordValidator;

    @Autowired
    public PasswordProviderImpl(PasswordValidator passwordValidator, PassayPasswordGenerator passayPasswordGenerator) {
        this.passwordValidator = passwordValidator;
        this.passayPasswordGenerator = passayPasswordGenerator;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public String encodePassword(String cleartextPassword) {
        return this.bCryptPasswordEncoder.encode(cleartextPassword);
    }

    public String validatePasswordAndEncode(String cleartextPassword) throws InvalidPasswordException {
        if (passwordValidator.isCleartextPasswordValid(cleartextPassword)) {
            return this.bCryptPasswordEncoder.encode(cleartextPassword);
        } else {
            throw new InvalidPasswordException("Password must fulfill security requirements.");
        }
    }

    @Override
    public String generatePassword() {
        return passayPasswordGenerator.generatePassword();
    }

}
