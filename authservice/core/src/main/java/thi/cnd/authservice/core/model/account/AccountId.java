package thi.cnd.authservice.core.model.account;

import java.util.UUID;

public record AccountId(
        UUID id
) {
   /* public static AccountId create() {
        return new AccountId(UUID.randomUUID());
    }*/

    public AccountId() {
        this(UUID.randomUUID());
    }

    public static AccountId of(String uuid) {
        return new AccountId(UUID.fromString(uuid));
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
