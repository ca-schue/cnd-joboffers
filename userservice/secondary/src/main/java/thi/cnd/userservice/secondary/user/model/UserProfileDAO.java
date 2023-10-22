package thi.cnd.userservice.secondary.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.mongodb.core.index.Indexed;

public record UserProfileDAO(
        @Indexed(unique = true) @Email String email,
        @NotBlank String firstName,
        @NotBlank String lastName
) {
}
