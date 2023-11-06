package thi.cnd.careerservice;

public class NotYetProcessedException extends RuntimeException {

    public NotYetProcessedException() {
        super("The changes made were not yet processed.");
    }

    public NotYetProcessedException(String message) {
        super(message);
    }

}
