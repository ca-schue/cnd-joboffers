package thi.cnd.userservice.core.port.secondary.repository;

import jakarta.validation.constraints.NotNull;
import thi.cnd.userservice.core.exception.UserAlreadyExistsException;
import thi.cnd.userservice.core.exception.UserNotFoundByEmailException;
import thi.cnd.userservice.core.exception.UserNotFoundByIdException;
import thi.cnd.userservice.core.model.company.CompanyId;
import thi.cnd.userservice.core.model.user.User;
import thi.cnd.userservice.core.model.user.UserId;

public interface UserRepositoryPort {
    @NotNull User findUserById(@NotNull UserId userId) throws UserNotFoundByIdException;

    @NotNull User findUserByEmail(@NotNull String email) throws UserNotFoundByEmailException;

    @NotNull User updateOrSaveUser(User user);

    void deleteUserById(@NotNull UserId userId) throws UserNotFoundByIdException;

    void deleteUserByEmail(@NotNull String email) throws UserNotFoundByEmailException;

    @NotNull User saveUser(@NotNull User user) throws UserAlreadyExistsException;

    void removeCompanyFromUser(CompanyId companyId);

}
