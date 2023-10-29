package thi.cnd.careerservice.user.model;

import java.util.Set;

import org.springframework.validation.annotation.Validated;

import thi.cnd.careerservice.company.model.CompanyId;
import jakarta.validation.constraints.NotNull;

/**
 * User that exists in the user-service
 */
@Validated
public record User(
    @NotNull UserId id,
    @NotNull Set<CompanyId> memberOf,
    @NotNull boolean isSubscribed
) {
}
