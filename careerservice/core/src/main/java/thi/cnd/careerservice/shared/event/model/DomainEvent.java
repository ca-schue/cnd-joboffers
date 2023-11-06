package thi.cnd.careerservice.shared.event.model;

public sealed interface DomainEvent permits JobApplicationEvent, JobOfferEvent {
}
