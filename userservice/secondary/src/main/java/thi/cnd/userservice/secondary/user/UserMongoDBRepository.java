package thi.cnd.userservice.secondary.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import thi.cnd.userservice.core.model.user.UserId;
import thi.cnd.userservice.secondary.user.model.UserDAO;

import java.util.Optional;

public interface UserMongoDBRepository extends MongoRepository<UserDAO, UserId> {

    Optional<UserDAO> findByProfileEmail(String email);

    Optional<UserDAO> deleteOneById(UserId id);

    Optional<UserDAO> deleteOneByProfileEmail(String email);

}
