package thi.cnd.userservice.core.model.company;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record CompanyLinks(
        @Nullable String websiteUrl,
        @NotNull Set<String> socialMedia
) {
    public CompanyLinks() {
        this(null, Set.of());
    }
}
