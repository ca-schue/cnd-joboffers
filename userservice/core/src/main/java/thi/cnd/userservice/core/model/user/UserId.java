package thi.cnd.userservice.core.model.user;


import java.util.UUID;

public record UserId(
        UUID id
) {
   /* public static AccountId create() {
        return new AccountId(UUID.randomUUID());
    }*/

    public UserId() {
        this(UUID.randomUUID());
    }

    public static UserId of(String uuid) {
        return new UserId(UUID.fromString(uuid));
    }

    @Override
    public String toString() {
        return id.toString();
    }
}