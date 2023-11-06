package thi.cnd.careerservice.event.internal.exception;

public class EventStoreConnectionException extends RuntimeException {
    public EventStoreConnectionException(Throwable e) {
        super(e);
    }
}
