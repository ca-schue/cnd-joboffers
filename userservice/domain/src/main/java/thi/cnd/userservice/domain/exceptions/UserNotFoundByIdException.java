package thi.cnd.userservice.domain.exceptions;

import thi.cnd.userservice.domain.model.user.UserId;

public class UserNotFoundByIdException extends Exception{
    public UserNotFoundByIdException(UserId userId) {
        super("User " + userId.toString() + " was not found.");
    }
}
