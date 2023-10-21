package thi.cnd.userservice.core.model.company;


import java.util.UUID;

public record CompanyId(
        UUID id
) {
   /* public static AccountId create() {
        return new AccountId(UUID.randomUUID());
    }*/

    public CompanyId() {
        this(UUID.randomUUID());
    }

    public static thi.cnd.userservice.core.model.user.UserId of(String uuid) {
        return new thi.cnd.userservice.core.model.user.UserId(UUID.fromString(uuid));
    }

    @Override
    public String toString() {
        return id.toString();
    }
}