package thi.cnd.userservice.secondary.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import thi.cnd.userservice.core.exception.UserAlreadyExistsException;
import thi.cnd.userservice.core.exception.UserNotFoundByEmailException;
import thi.cnd.userservice.core.exception.UserNotFoundByIdException;
import thi.cnd.userservice.core.ports.secondary.UserRepositoryPort;
import thi.cnd.userservice.core.model.user.User;
import thi.cnd.userservice.core.model.user.UserId;
import thi.cnd.userservice.secondary.user.model.UserDAO;
import thi.cnd.userservice.secondary.user.model.UserDaoMapper;

@Service
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserDaoMapper mapper;
    private final UserMongoDBRepository repository;

    @Override
    public User findUserById(UserId userId) throws UserNotFoundByIdException {
        return repository
                .findById(userId)
                .map(mapper::toUser)
                .orElseThrow(() -> new UserNotFoundByIdException(userId));
    }

    @Override
    public User findUserByEmail(String email) throws UserNotFoundByEmailException {
        return repository
                .findByProfileEmail(email)
                .map(mapper::toUser)
                .orElseThrow(() -> new UserNotFoundByEmailException(email));
    }

    @Override
    public User updateOrSaveUser(User updatedUser) {
        try {
            findUserById(updatedUser.getId());
        } catch (UserNotFoundByIdException e) {
            throw new RuntimeException("No user with id " + updatedUser.getId() + " to update"); // TODO: better solution?
        }
        UserDAO updatedUserDao = mapper.toDAO(updatedUser);
        UserDAO savedUpdatedUserDao = repository.save(updatedUserDao); // put & post
        return mapper.toUser(savedUpdatedUserDao);
    }

    @Override
    public void deleteUserById(UserId userId) throws UserNotFoundByIdException {
        repository.deleteOneById(userId).orElseThrow(() -> new UserNotFoundByIdException(userId));
    }

    @Override
    public void deleteUserByEmail(String email) throws UserNotFoundByEmailException {
        repository.deleteOneByProfileEmail(email).orElseThrow(() -> new UserNotFoundByEmailException(email));

    }

    @Override
    public User saveUser(User user) throws UserAlreadyExistsException {
        try {
            UserDAO dao = mapper.toDAO(user);
            UserDAO savedDao = repository.save(dao);
            return mapper.toUser(savedDao);
        } catch (DuplicateKeyException e) {
            throw new UserAlreadyExistsException("User profile already linked to this account.");
        }
    }
}
