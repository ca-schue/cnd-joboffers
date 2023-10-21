package thi.cnd.userservice.core.exception;

import thi.cnd.userservice.core.model.user.UserId;

public class UserNotFoundByIdException extends Exception{
    public UserNotFoundByIdException(UserId userId) {
        super("User " + userId.toString() + " was not found.");
    }
}
