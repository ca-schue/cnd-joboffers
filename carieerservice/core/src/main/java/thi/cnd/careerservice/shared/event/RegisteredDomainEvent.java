package thi.cnd.careerservice.shared.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate all events that should be able to be fired and retrieved from an {@link EventStore}
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RegisteredDomainEvent {
    String name();
}
