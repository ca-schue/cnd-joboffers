package thi.cnd.careerservice.joboffer.query.application.service;

import java.util.function.Consumer;

import org.springframework.stereotype.Component;

import thi.cnd.careerservice.joboffer.query.port.JobOfferUpdateViewPort;
import thi.cnd.careerservice.shared.event.model.JobOfferEvent;
import thi.cnd.careerservice.shared.event.model.RecordedDomainEventWithMetadata;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferId;
import thi.cnd.careerservice.joboffer.query.domain.model.JobOfferView;
import thi.cnd.careerservice.joboffer.query.port.JobOfferQueryPort;
import thi.cnd.careerservice.shared.view.model.ViewEventMetadata;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DefaultUpdateJobOfferViewService implements UpdateJobOfferViewService {

    private final JobOfferUpdateViewPort jobOfferUpdateViewPort;
    private final JobOfferQueryPort jobOfferQueryPort;

    @Override
    public JobOfferView newJobOfferCreated(RecordedDomainEventWithMetadata<JobOfferEvent.JobOfferCreated> eventWithMetadata) {
        return jobOfferUpdateViewPort.createNewJobOffer(new JobOfferView(eventWithMetadata));
    }

    @Override
    public JobOfferView updateJobOfferStatus(RecordedDomainEventWithMetadata<JobOfferEvent.JobOfferStatusUpdated> eventWithMetadata) {
        var data = eventWithMetadata.data();
        return getUpdateAndSave(data.id(), eventWithMetadata, jobOffer -> jobOffer.setStatus(data.status()));
    }

    @Override
    public JobOfferView updateJobOfferTitle(RecordedDomainEventWithMetadata<JobOfferEvent.JobOfferTitleChanged> eventWithMetadata) {
        var data = eventWithMetadata.data();
        return getUpdateAndSave(data.id(), eventWithMetadata, jobOffer -> jobOffer.setTitle(data.title()));
    }

    @Override
    public JobOfferView updateJobOfferAttributes(RecordedDomainEventWithMetadata<JobOfferEvent.JobOfferAttributesChanged> eventWithMetadata) {
        var data = eventWithMetadata.data();
        return getUpdateAndSave(data.id(), eventWithMetadata, jobOffer -> {
            jobOffer.setDescription(data.description());
            jobOffer.setTags(data.tags());
            jobOffer.setSalaryRange(data.salaryRange());
        });
    }

    @Override
    public JobOfferView deleteJobOffer(RecordedDomainEventWithMetadata<JobOfferEvent.JobOfferDeleted> event) {
        return jobOfferUpdateViewPort.deleteJobOffer(event.data().id());
    }

    private <E extends JobOfferEvent> JobOfferView getUpdateAndSave(
        JobOfferId id, RecordedDomainEventWithMetadata<E> eventWithMetadata, Consumer<JobOfferView> consumer
    ) {
        var jobOffer = jobOfferQueryPort.getJobOffer(id);
        consumer.accept(jobOffer);
        jobOffer.setMetadata(new ViewEventMetadata(eventWithMetadata.metaData()));
        return jobOfferUpdateViewPort.save(jobOffer);
    }

}
