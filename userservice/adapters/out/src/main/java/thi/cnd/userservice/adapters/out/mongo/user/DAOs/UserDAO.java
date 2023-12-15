package thi.cnd.userservice.adapters.out.mongo.user.DAOs;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import thi.cnd.userservice.domain.model.user.UserId;

@Document(collection = "Users")
public record UserDAO(
        @Id UserId id,
        @NotNull UserProfileDAO profile,
        @NotNull UserCompanyAssociationDAO associations,
        @NotNull UserSettingsDAO settings,
        @NotNull UserSubscriptionDAO subscription
) {
}
