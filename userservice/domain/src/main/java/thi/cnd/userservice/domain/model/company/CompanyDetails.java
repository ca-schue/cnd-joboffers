package thi.cnd.userservice.domain.model.company;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record CompanyDetails(
        @NotBlank String name,
        @NotBlank String description,
        @NotNull Set<String> tags,
        @Nullable String location
) {
}
