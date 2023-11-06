package thi.cnd.careerservice.shared;

import java.util.Set;
import java.util.UUID;

import thi.cnd.careerservice.api.generated.model.CompanyDTO;
import thi.cnd.careerservice.api.generated.model.CompanyDetailsDTO;
import thi.cnd.careerservice.api.generated.model.CompanyLinksDTO;
import thi.cnd.careerservice.api.generated.model.CompanyPartnerProgramDTO;
import thi.cnd.careerservice.api.generated.model.UserCompanyAssociationDTO;
import thi.cnd.careerservice.api.generated.model.UserDTO;
import thi.cnd.careerservice.api.generated.model.UserProfileDTO;
import thi.cnd.careerservice.api.generated.model.UserSettingsDTO;
import thi.cnd.careerservice.api.generated.model.UserSubscriptionDTO;

public class UserServiceTestProvider {

    private UserServiceTestProvider() {}

    public static final String USER_EMAIL = "test@test.com";
    public static final String FIRST_NAME = "Guenther";
    public static final String LAST_NAME = "Jauch";

    public static UserDTO buildUserResponse(UUID userId) {
        return new UserDTO()
            .id(userId)
            .profile(
                new UserProfileDTO()
                    .userProfileEmail(USER_EMAIL)
                    .firstName(FIRST_NAME)
                    .lastName(LAST_NAME)
            )
            .associations(new UserCompanyAssociationDTO())
            .settings(new UserSettingsDTO().nightModeActive(false))
            .subscription(new UserSubscriptionDTO().subscribed(false).subscribedUntil(null));
    }

    public static CompanyDTO buildCompanyResponse(UUID companyId, UUID ownerId) {
        return new CompanyDTO()
            .id(companyId)
            .owner(ownerId)
            .details(new CompanyDetailsDTO().name("").description("").tags(Set.of()).location(""))
            .links(new CompanyLinksDTO().socialMedia(Set.of()).websiteUrl(""))
            .partnerProgram(new CompanyPartnerProgramDTO().partnered(false).partnerUntil(null));
    }

}
