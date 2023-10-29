package thi.cnd.careerservice.shared.view;

import thi.cnd.careerservice.shared.event.InternalDomainEventListener;
import thi.cnd.careerservice.shared.event.model.DomainEvent;
import thi.cnd.careerservice.shared.view.model.View;

/**
 * Projects {@link DomainEvent}s to the {@link View}.
 */
public interface ViewProjector extends InternalDomainEventListener {

    ViewProjectorName getUniqueViewName();

    default String getUniqueListenerName() {
        return getUniqueViewName().toString();
    }

}
