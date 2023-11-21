package thi.cnd.authservice.domain.password;

import jakarta.validation.constraints.NotBlank;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class PasswordGenerator {

    private final org.passay.PasswordGenerator passwordGenerator = new org.passay.PasswordGenerator();
    private final CharacterRule digitRule = new CharacterRule(EnglishCharacterData.Digit);
    private final CharacterRule alphabeticalRule = new CharacterRule(EnglishCharacterData.Alphabetical);
    private final int minPasswordCharacterLength;
    private final int maxPasswordCharacterLength;

    public PasswordGenerator(
            @Value("${password.minCharacterLength}") int minPasswordCharacterLength,
            @Value("${password.maxCharacterLength}") int maxPasswordCharacterLength
    ) {
        this.minPasswordCharacterLength = minPasswordCharacterLength;
        this.maxPasswordCharacterLength = maxPasswordCharacterLength;
    }

    private final CharacterData specialCharacterData = new CharacterData() {
        @Override
        public String getErrorCode() {
            return "INSUFFICIENT_SPECIAL";
        }

        @Override
        public String getCharacters() {
            return "~!@#^&*()_-+=[]:;<,>.?/";
        }
    };
    private final CharacterRule specialRule = new CharacterRule(specialCharacterData);
    private final Random random = new Random();

    @NotBlank
    public String generatePassword() {
        var length = random.nextInt(minPasswordCharacterLength, maxPasswordCharacterLength + 1);
        return passwordGenerator.generatePassword(length, digitRule, alphabeticalRule, specialRule);
    }

}
