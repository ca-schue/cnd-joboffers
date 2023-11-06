package thi.cnd.careerservice.company.model;

import java.util.Set;

import org.springframework.validation.annotation.Validated;

import thi.cnd.careerservice.user.model.UserId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Company that exists in the user-service
 */
@Validated
public record Company(
    @NotNull CompanyId id,
    @NotNull UserId owner,
    @NotNull Set<UserId> members,
    @NotNull String name,
    @NotBlank String description,
    @NotBlank String location,
    @NotNull boolean isPartnered
) {
}
