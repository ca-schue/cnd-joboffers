package thi.cnd.authservice.core.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record AccountAccessToken(
        @NotNull Account account,
        @NotBlank String accessToken,
        @NotNull Instant validUntil
) {
}
