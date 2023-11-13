package thi.cnd.userservice.domain.model.company;

import jakarta.annotation.Nullable;

import java.time.Instant;

public record CompanyPartnerProgram(
        @Nullable Instant partnerUntil
) {

    public CompanyPartnerProgram() {
        this(null);
    }

    public boolean isPartnered() {
        return partnerUntil != null && Instant.now().isBefore(partnerUntil);
    }

}