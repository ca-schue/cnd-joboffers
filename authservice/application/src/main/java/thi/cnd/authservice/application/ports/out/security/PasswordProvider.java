package thi.cnd.authservice.application.ports.out.security;

import thi.cnd.authservice.domain.exceptions.InvalidPasswordException;

public interface PasswordProvider {

    public String validatePasswordAndEncode(String cleartextPassword) throws InvalidPasswordException;
    public String generatePassword();
    public String encodePassword(String cleartextPassword);
}
