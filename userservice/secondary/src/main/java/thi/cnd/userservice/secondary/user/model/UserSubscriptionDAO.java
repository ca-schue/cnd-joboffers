package thi.cnd.userservice.secondary.user.model;

import jakarta.annotation.Nullable;

import java.time.Instant;

public record UserSubscriptionDAO(
        @Nullable Instant subscribedUntil
) {
}
