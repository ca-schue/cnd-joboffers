package thi.cnd.careerservice.userservice;

import java.util.stream.Collectors;

import thi.cnd.careerservice.api.generated.model.CompanyDTO;
import thi.cnd.careerservice.api.generated.model.UserDTO;
import thi.cnd.careerservice.company.model.Company;
import thi.cnd.careerservice.user.model.UserId;
import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.user.model.User;
import jakarta.annotation.Nonnull;

public class UserServiceApiMapper {

    private UserServiceApiMapper() {}

    @Nonnull
    public static User toUser(@Nonnull UserDTO response) {
        return new User(
            new UserId(response.getId()),
            response.getAssociations().getMemberOf().stream().map(CompanyId::new).collect(Collectors.toSet()),
            response.getSubscription().getSubscribed()
        );
    }

    @Nonnull
    public static Company toCompany( @Nonnull CompanyDTO dto) {
        return new Company(
            new CompanyId(dto.getId()),
            new UserId(dto.getOwner()),
            dto.getMembers().stream().map(UserId::new).collect(Collectors.toSet()),
            dto.getDetails().getName(),
            dto.getDetails().getDescription(),
            dto.getDetails().getLocation(),
            dto.getPartnerProgram().getPartnered()
        );
    }
}
