package thi.cnd.userservice.adapters.out.mongo.user.DAOs;

import jakarta.annotation.Nullable;

import java.time.Instant;

public record UserSubscriptionDAO(
        @Nullable Instant subscribedUntil
) {
}
