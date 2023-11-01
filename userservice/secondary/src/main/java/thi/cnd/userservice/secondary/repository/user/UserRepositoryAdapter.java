package thi.cnd.userservice.secondary.repository.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import thi.cnd.userservice.core.exception.UserAlreadyExistsException;
import thi.cnd.userservice.core.exception.UserNotFoundByEmailException;
import thi.cnd.userservice.core.exception.UserNotFoundByIdException;
import thi.cnd.userservice.core.model.company.CompanyId;
import thi.cnd.userservice.core.model.user.UserCompanyAssociation;
import thi.cnd.userservice.core.port.secondary.repository.UserRepositoryPort;
import thi.cnd.userservice.core.model.user.User;
import thi.cnd.userservice.core.model.user.UserId;
import thi.cnd.userservice.secondary.repository.user.model.UserDAO;
import thi.cnd.userservice.secondary.repository.user.model.UserDaoMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserMongoDBRepository repository;
    private final UserDaoMapper mapper;
    @Override
    public User findUserById(UserId userId) throws UserNotFoundByIdException {
        return repository
                .findById(userId)
                .map(mapper::toUser)
                .orElseThrow(() -> new UserNotFoundByIdException(userId));
    }

    @Override
    public User findUserByEmail(String email) throws UserNotFoundByEmailException {
        List<UserDAO> foundUsers = repository.customFindByEmail(email);
        if (foundUsers.isEmpty()) {
            throw new UserNotFoundByEmailException(email);
        } else if (foundUsers.size() > 1) {
            throw new RuntimeException("Index violation: > 1 User with same user profile mail");
        } else {
            return mapper.toUser(foundUsers.get(0));
        }
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

    @Override
    public void removeCompanyFromUser(CompanyId companyId, UserId ownerId) throws UserNotFoundByIdException {
        User owner = findUserById(ownerId);
        UserCompanyAssociation oca = owner.getAssociations();
        oca.setOwnerOf(null);
        owner.setAssociations(oca);
        updateOrSaveUser(owner);
        repository.removeCompanyFromAllMembers(companyId);
        repository.removeCompanyFromAllInvitedUsers(companyId);
    }
}
