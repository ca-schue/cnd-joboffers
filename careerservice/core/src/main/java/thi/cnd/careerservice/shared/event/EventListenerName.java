package thi.cnd.careerservice.shared.event;

import lombok.RequiredArgsConstructor;

/**
 * The unique name of an {@link EventListener}
 */
@RequiredArgsConstructor
public enum EventListenerName {

    JOB_APPLICATION("job-application-event-controller");

    private final String value;

    @Override
    public String toString() {
        return this.value;
    }

}
