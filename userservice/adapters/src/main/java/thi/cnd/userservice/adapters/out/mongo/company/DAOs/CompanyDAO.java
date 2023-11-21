package thi.cnd.userservice.adapters.out.mongo.company.DAOs;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;
import thi.cnd.userservice.domain.model.company.CompanyId;
import thi.cnd.userservice.domain.model.user.UserId;

import java.util.Set;

@Validated
@Document(collection = "Companies")
public record CompanyDAO(
        @Id @NotNull CompanyId id,
        @Indexed(unique = true, background = true) UserId owner,
        @NotNull Set<UserId> members,
        @NotNull CompanyDetailsDAO details,
        @NotNull CompanyLinksDAO links,
        @NotNull CompanyPartnerProgramDAO partnerProgram
) {
}