package thi.cnd.careerservice.joboffer.command;

import org.springframework.stereotype.Service;

import thi.cnd.careerservice.joboffer.event.JobOfferEventStore;
import thi.cnd.careerservice.shared.model.DataWithETag;
import thi.cnd.careerservice.shared.model.ETag;
import thi.cnd.careerservice.joboffer.domain.JobOfferDomainService;
import thi.cnd.careerservice.joboffer.model.JobOffer;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DefaultJobOfferCommandHandler implements JobOfferCommandHandler {

    private final JobOfferDomainService jobOfferDomainService;
    private final JobOfferEventStore eventStore;


    @Override
    public DataWithETag<JobOffer> createJobOffer(JobOfferCommand.CreateJobOffer command) {
        var newJobOffer = jobOfferDomainService.createJobOffer(command);
        return eventStore.publishFirstEventAndCreateNewEventStream(newJobOffer);
    }

    @Override
    public DataWithETag<JobOffer> updateJobOfferStatus(JobOfferCommand.UpdateJobOfferStatus command, ETag expectedRevision) {
        return eventStore.updateAndPublish(command.id(), expectedRevision, jobOffer -> jobOffer.setStatus(command.status()));
    }

    @Override
    public DataWithETag<JobOffer> updateJobOfferAttributes(JobOfferCommand.UpdateJobOfferAttributes command, ETag expectedRevision) {
        return eventStore.updateAndPublish(command.id(), expectedRevision, jobOffer -> {
            jobOffer.setTitle(command.title());
            jobOffer.setAttributes(command.description(), command.tags(), command.salaryRange());
        });
    }

    @Override
    public DataWithETag<JobOffer> deleteJobOffer(JobOfferCommand.DeleteJobOffer command, ETag expectedRevision) {
        return eventStore.updateAndPublish(command.id(), expectedRevision, JobOffer::delete);
    }


}
