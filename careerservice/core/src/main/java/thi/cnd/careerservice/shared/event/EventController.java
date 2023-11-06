package thi.cnd.careerservice.shared.event;

public interface EventController extends InternalDomainEventListener {

    EventControllerName getControllerName();

    default String getUniqueListenerName() {
        return getControllerName().toString();
    }

}
