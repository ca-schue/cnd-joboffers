package thi.cnd.careerservice.user;

import org.springframework.validation.annotation.Validated;

import thi.cnd.careerservice.user.model.User;
import thi.cnd.careerservice.user.model.UserId;
import jakarta.validation.constraints.NotNull;

/**
 * Port that calls the user-service to retrieve user data
 */
@Validated
public interface UserPort {

    @NotNull User getUser(@NotNull UserId userId);

}
