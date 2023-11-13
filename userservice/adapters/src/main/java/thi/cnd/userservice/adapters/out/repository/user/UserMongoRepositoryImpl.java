package thi.cnd.userservice.adapters.out.repository.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import thi.cnd.userservice.adapters.out.repository.user.DAOs.*;
import thi.cnd.userservice.adapters.out.repository.user.DAOs.UserDaoMapper;
import thi.cnd.userservice.application.ports.out.repository.UserRepository;
import thi.cnd.userservice.domain.exceptions.*;
import thi.cnd.userservice.domain.model.company.*;
import thi.cnd.userservice.domain.model.user.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMongoRepositoryImpl implements UserRepository {

    private final CustomUserMongoRepository repository;
    private final UserDaoMapper userDaoMapper;
    @Override
    public User findUserById(UserId userId) throws UserNotFoundByIdException {
        return repository
                .findById(userId)
                .map(userDaoMapper::toUser)
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
            return userDaoMapper.toUser(foundUsers.get(0));
        }
    }

    @Override
    public User updateOrSaveUser(User updatedUser) {
        try {
            findUserById(updatedUser.getId());
        } catch (UserNotFoundByIdException e) {
            throw new RuntimeException("No user with id " + updatedUser.getId() + " to update"); // TODO: better solution?
        }
        UserDAO updatedUserDao = userDaoMapper.toDAO(updatedUser);
        UserDAO savedUpdatedUserDao = repository.save(updatedUserDao); // put & post
        return userDaoMapper.toUser(savedUpdatedUserDao);
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
            UserDAO dao = userDaoMapper.toDAO(user);
            UserDAO savedDao = repository.save(dao);
            return userDaoMapper.toUser(savedDao);
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
