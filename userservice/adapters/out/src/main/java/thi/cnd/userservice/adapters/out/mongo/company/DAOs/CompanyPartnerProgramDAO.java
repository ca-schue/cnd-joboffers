package thi.cnd.userservice.adapters.out.mongo.company.DAOs;

import jakarta.annotation.Nullable;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;

@Validated
public record CompanyPartnerProgramDAO(
        @Nullable Instant partnerUntil
) {
}
