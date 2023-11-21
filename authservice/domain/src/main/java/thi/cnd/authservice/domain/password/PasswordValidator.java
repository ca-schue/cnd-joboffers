package thi.cnd.authservice.domain.password;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class PasswordValidator {
    public static final int PASSWORD_MIN_LENGTH = 8;

    /**
     * Require at least 8 characters, one letter, one digit, and one special character
     */
    public static final Pattern PASSWORD_REGEX = Pattern.compile(
    "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=!?_{}\\\"§$%&/()]).{" + PASSWORD_MIN_LENGTH + ",}$");

    public boolean isCleartextPasswordValid(String password) {
        return password.length() >= PASSWORD_MIN_LENGTH && PASSWORD_REGEX.matcher(password).matches();
    }
}
