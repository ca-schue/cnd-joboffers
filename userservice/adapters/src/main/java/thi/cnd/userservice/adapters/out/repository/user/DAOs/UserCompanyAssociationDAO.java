package thi.cnd.userservice.adapters.out.repository.user.DAOs;

import com.mongodb.lang.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import thi.cnd.userservice.domain.model.company.CompanyId;

import java.util.Set;

@Validated
public record UserCompanyAssociationDAO(

        @NotNull Set<CompanyId> memberOf,

        @NotNull Set<CompanyId> invitedTo,

        @Nullable CompanyId ownerOf
) {
}
