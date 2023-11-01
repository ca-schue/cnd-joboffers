package thi.cnd.userservice.secondary.repository.user.model;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import thi.cnd.userservice.core.model.user.UserId;

@Document(collection = "Users")
public record UserDAO(
        @Id UserId id,
        @NotNull UserProfileDAO profile,
        @NotNull UserCompanyAssociationDAO associations,
        @NotNull UserSettingsDAO settings,
        @NotNull UserSubscriptionDAO subscription
) {
}
