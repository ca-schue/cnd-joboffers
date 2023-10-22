package thi.cnd.userservice.secondary.repository.user.model;

import jakarta.annotation.Nullable;

import java.time.Instant;

public record UserSubscriptionDAO(
        @Nullable Instant subscribedUntil
) {
}
