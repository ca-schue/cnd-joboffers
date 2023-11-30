package thi.cnd.careerservice.joboffer.command.application.service;

import org.springframework.stereotype.Service;

import thi.cnd.careerservice.joboffer.command.application.model.JobOfferCommand;
import thi.cnd.careerservice.joboffer.command.port.JobOfferEventStore;
import thi.cnd.careerservice.joboffer.command.port.JobOfferCommandHandler;
import thi.cnd.careerservice.shared.model.DataWithETag;
import thi.cnd.careerservice.shared.model.ETag;
import thi.cnd.careerservice.joboffer.command.domain.service.JobOfferDomainService;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOffer;
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
