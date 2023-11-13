package thi.cnd.userservice.domain.model.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;


@Getter
@Setter
@Validated
@AllArgsConstructor
public class UserProfile {
    private @Email String email;
    private @NotBlank String firstName;
    private @NotBlank String lastName;

}
