package thi.cnd.careerservice.shared.event;

public interface EventListener extends InternalDomainEventListener {

    EventListenerName getListenerName();

    default String getUniqueListenerName() {
        return getListenerName().toString();
    }

}
