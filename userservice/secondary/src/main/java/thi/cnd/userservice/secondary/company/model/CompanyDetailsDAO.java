package thi.cnd.userservice.secondary.company.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.validation.annotation.Validated;

import java.util.Set;

@Validated
public record CompanyDetailsDAO(
        @Indexed(unique = true, background = true) @NotBlank String name,
        @NotBlank String description,
        @Indexed(background = true) @NotNull Set<String> tags,
        @Indexed(background = true) String location
) {
}
