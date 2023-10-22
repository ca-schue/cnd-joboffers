package thi.cnd.userservice.secondary.repository.company.model;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.Set;

@Validated
public record CompanyLinksDAO(
        @Nullable String websiteUrl,
        @NotNull Set<String> socialMedia
) {
}
