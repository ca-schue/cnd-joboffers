package thi.cnd.userservice.domain.model.user;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class UserId {

    private final UUID id;

    /*public UserId() {
        id = UUID.randomUUID();
    }*/

   /* public static AccountId create() {
        return new AccountId(UUID.randomUUID());
    }*/

    public static UserId generateUserId() {
        return new UserId(UUID.randomUUID());
    }

    public static UserId of(String uuid) {
        return new UserId(UUID.fromString(uuid));
    }

    @Override
    public String toString() {
        return id.toString();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof UserId)) {
            return false;
        }
        UserId other = (UserId) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}