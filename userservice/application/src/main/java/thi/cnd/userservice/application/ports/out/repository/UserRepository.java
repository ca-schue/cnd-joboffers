package thi.cnd.userservice.application.ports.out.repository;

import jakarta.validation.constraints.NotNull;
import thi.cnd.userservice.domain.exceptions.UserAlreadyExistsException;
import thi.cnd.userservice.domain.exceptions.UserNotFoundByEmailException;
import thi.cnd.userservice.domain.exceptions.UserNotFoundByIdException;
import thi.cnd.userservice.domain.model.company.*;
import thi.cnd.userservice.domain.model.user.*;

public interface UserRepository {
    @NotNull User findUserById(@NotNull UserId userId) throws UserNotFoundByIdException;

    @NotNull User findUserByEmail(@NotNull String email) throws UserNotFoundByEmailException;

    @NotNull User updateOrSaveUser(User user);

    void deleteUserById(@NotNull UserId userId) throws UserNotFoundByIdException;

    void deleteUserByEmail(@NotNull String email) throws UserNotFoundByEmailException;

    @NotNull User saveUser(@NotNull User user) throws UserAlreadyExistsException;

    void removeCompanyFromUser(CompanyId companyId, UserId ownerId) throws UserNotFoundByIdException;

}
