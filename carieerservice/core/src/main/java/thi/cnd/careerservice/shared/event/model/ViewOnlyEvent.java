package thi.cnd.careerservice.shared.event.model;

/**
 * Events that are persisted in the event store, but do not have any mutating effect on the aggregate itself.
 */
public sealed interface ViewOnlyEvent permits JobApplicationEvent.JobApplicationViewOnlyEvent {
}
