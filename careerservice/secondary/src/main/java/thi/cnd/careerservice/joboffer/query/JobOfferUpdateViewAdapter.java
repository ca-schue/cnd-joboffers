package thi.cnd.careerservice.joboffer.query;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import thi.cnd.careerservice.exception.ResourceAlreadyExistsException;
import thi.cnd.careerservice.exception.ViewResourceNotFoundException;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferId;
import thi.cnd.careerservice.joboffer.query.mapper.JobOfferViewDaoMapper;
import thi.cnd.careerservice.joboffer.query.domain.model.JobOfferView;
import thi.cnd.careerservice.joboffer.query.port.JobOfferUpdateViewPort;
import thi.cnd.careerservice.joboffer.query.repository.JobOfferViewRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JobOfferUpdateViewAdapter implements JobOfferUpdateViewPort {

    private final JobOfferViewRepository repository;

    @Override
    public JobOfferView createNewJobOffer(JobOfferView newView) {
        try {
            var dao = JobOfferViewDaoMapper.toDAO(newView);
            var savedDao = repository.insert(dao);
            return JobOfferViewDaoMapper.toJobOffer(savedDao);
        } catch (DuplicateKeyException e) {
            throw new ResourceAlreadyExistsException("The job offer " + newView.getId() + " already exists in the view");
        }
    }

    @Override
    public JobOfferView save(JobOfferView jobOfferView) {
        var dao = JobOfferViewDaoMapper.toDAO(jobOfferView);
        var savedDao = repository.save(dao);
        return JobOfferViewDaoMapper.toJobOffer(savedDao);
    }

    @Override
    public JobOfferView deleteJobOffer(JobOfferId id) {
        return repository.deleteOneById(id).map(JobOfferViewDaoMapper::toJobOffer)
            .orElseThrow(() -> new ViewResourceNotFoundException("JobOffer", id));
    }

}
