package thi.cnd.authservice.core.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClientWithPlaintextPassword(
        @NotNull Client client,
        @NotBlank String password) {}

