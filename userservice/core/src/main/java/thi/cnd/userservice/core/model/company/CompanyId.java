package thi.cnd.userservice.core.model.company;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CompanyId {

    private final UUID id;

    /*public CompanyId() {
        this.id = UUID.randomUUID();
    }*/

   /* public static AccountId create() {
        return new AccountId(UUID.randomUUID());
    }*/

    public static CompanyId generateCompanyId() {
        return new CompanyId(UUID.randomUUID());
    }

    public static CompanyId of(String uuid) {
        return new CompanyId(UUID.fromString(uuid));
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
        if (!(object instanceof CompanyId)) {
            return false;
        }
        CompanyId other = (CompanyId) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}