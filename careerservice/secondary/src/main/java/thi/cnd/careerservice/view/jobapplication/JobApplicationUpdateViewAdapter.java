package thi.cnd.careerservice.view.jobapplication;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import thi.cnd.careerservice.shared.event.model.RecordedEventMetadata;
import thi.cnd.careerservice.exception.ResourceAlreadyExistsException;
import thi.cnd.careerservice.exception.ViewResourceNotFoundException;
import thi.cnd.careerservice.jobapplication.model.JobApplicationId;
import thi.cnd.careerservice.jobapplication.model.JobApplicationStatus;
import thi.cnd.careerservice.jobapplication.view.model.JobApplicationView;
import thi.cnd.careerservice.jobapplication.view.update.JobApplicationUpdateViewPort;
import thi.cnd.careerservice.shared.view.model.ViewEventMetadata;
import thi.cnd.careerservice.view.jobapplication.mapper.JobApplicationViewDaoMapper;
import thi.cnd.careerservice.view.jobapplication.repository.JobApplicationViewRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JobApplicationUpdateViewAdapter implements JobApplicationUpdateViewPort {

    private final JobApplicationViewRepository repository;

    @Override
    public void createNewJobApplication(JobApplicationView view) {
        try {
            repository.insert(JobApplicationViewDaoMapper.toDAO(view));
        } catch (DuplicateKeyException e) {
            throw new ResourceAlreadyExistsException("The job application " + view.getId() + " already exists in the view");
        }
    }

    @Override
    public void deleteJobApplication(JobApplicationId id) {
        repository.deleteOneById(id).orElseThrow(() -> throwIfNotFound(id));
    }

    @Override
    public void updateJobOfferData(JobApplicationId id, String title, RecordedEventMetadata recordedEventMetadata) {
        repository.updateJobOfferData(id, title, new ViewEventMetadata(recordedEventMetadata))
            .orElseThrow(() -> throwIfNotFound(id));
    }

    @Override
    public void updateCompanyName(JobApplicationId id, String name, RecordedEventMetadata recordedEventMetadata) {
        repository.updateCompanyName(id, name, new ViewEventMetadata(recordedEventMetadata))
            .orElseThrow(() -> throwIfNotFound(id));
    }

    @Override
    public void updateStatus(JobApplicationId id, JobApplicationStatus status, RecordedEventMetadata recordedEventMetadata) {
        repository.updateStatus(id, status, new ViewEventMetadata(recordedEventMetadata))
            .orElseThrow(() -> throwIfNotFound(id));
    }

    @Override
    public void updateContent(JobApplicationId id, String content, RecordedEventMetadata recordedEventMetadata) {
        repository.updateContent(id, content, new ViewEventMetadata(recordedEventMetadata))
            .orElseThrow(() -> throwIfNotFound(id));
    }

    private ViewResourceNotFoundException throwIfNotFound(JobApplicationId id) {
        return new ViewResourceNotFoundException("JobApplication", id);
    }
}
