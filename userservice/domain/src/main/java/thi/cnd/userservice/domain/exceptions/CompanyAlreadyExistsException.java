package thi.cnd.userservice.domain.exceptions;

public class CompanyAlreadyExistsException extends Exception {
    public CompanyAlreadyExistsException(String message) {
        super(message);
    }
}
