package thi.cnd.userservice.secondary.repository.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import thi.cnd.userservice.core.model.company.CompanyId;
import thi.cnd.userservice.core.model.user.UserId;
import thi.cnd.userservice.secondary.repository.user.model.UserDAO;

import java.util.List;
import java.util.Optional;

public interface UserMongoDBRepository extends MongoRepository<UserDAO, UserId> {

    Optional<UserDAO> findByProfileEmail(String email);

    Optional<UserDAO> deleteOneById(UserId id);

    Optional<UserDAO> deleteOneByProfileEmail(String email);

    @Query("{'associations.memberOf': ?0}")
    List<UserDAO> findUsersByCompanyId(@Param("companyId") CompanyId companyId);

    @Query("{'associations.invitedTo': ?0}")
    List<UserDAO> findUsersByInvitedCompanyId(@Param("companyId") CompanyId companyId);

    default void removeCompanyFromAllMembers(CompanyId companyId) {
        List<UserDAO> users = findUsersByCompanyId(companyId);
        users.forEach(user -> user.associations().memberOf().remove(companyId));
        saveAll(users);
    }

    default void removeCompanyFromAllInvitedUsers(CompanyId companyId) {
        List<UserDAO> users = findUsersByInvitedCompanyId(companyId);
        users.forEach(user -> user.associations().invitedTo().remove(companyId));
        saveAll(users);
    }

}
