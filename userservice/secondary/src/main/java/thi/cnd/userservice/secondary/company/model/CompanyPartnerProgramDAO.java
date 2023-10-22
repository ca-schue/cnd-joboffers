package thi.cnd.userservice.secondary.company.model;

import jakarta.annotation.Nullable;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;

@Validated
public record CompanyPartnerProgramDAO(
        @Nullable Instant partnerUntil
) {
}
