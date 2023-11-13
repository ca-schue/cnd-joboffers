package thi.cnd.userservice.domain.model.user;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.time.Instant;

@Validated
public record UserSubscription(
        @With @Nullable Instant subscribedUntil
) {

    public UserSubscription() {
        this(null);
    }

    public boolean isSubscribed() {
        return subscribedUntil != null && Instant.now().isBefore(subscribedUntil);
    }

    public UserSubscription extendBy(@NotNull Duration duration) {
        if (this.subscribedUntil == null) {
            return withSubscribedUntil(Instant.now().plus(duration));
        }

        return withSubscribedUntil(this.subscribedUntil.plus(duration));
    }

}
