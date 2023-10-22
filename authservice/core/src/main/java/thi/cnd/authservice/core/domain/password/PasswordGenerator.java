package thi.cnd.authservice.core.domain.password;

import jakarta.validation.constraints.NotBlank;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class PasswordGenerator {

    private final org.passay.PasswordGenerator passwordGenerator = new org.passay.PasswordGenerator();
    private final CharacterRule digitRule = new CharacterRule(EnglishCharacterData.Digit);
    private final CharacterRule alphabeticalRule = new CharacterRule(EnglishCharacterData.Alphabetical);
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
        var length = random.nextInt(20, 26);
        return passwordGenerator.generatePassword(length, digitRule, alphabeticalRule, specialRule);
    }

}
