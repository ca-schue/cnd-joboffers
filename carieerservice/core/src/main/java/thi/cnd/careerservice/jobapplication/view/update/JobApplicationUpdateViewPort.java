package thi.cnd.careerservice.jobapplication.view.update;

import org.springframework.validation.annotation.Validated;

import thi.cnd.careerservice.shared.event.model.RecordedEventMetadata;
import thi.cnd.careerservice.jobapplication.model.JobApplicationId;
import thi.cnd.careerservice.jobapplication.model.JobApplicationStatus;
import thi.cnd.careerservice.jobapplication.view.model.JobApplicationView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Validated
public interface JobApplicationUpdateViewPort {

    @NotNull void createNewJobApplication(@NotNull JobApplicationView jobApplicationView);

    @NotNull void deleteJobApplication(@NotNull JobApplicationId id);

    @NotNull void updateJobOfferData(
        @NotNull JobApplicationId id, @NotBlank String title, @NotNull RecordedEventMetadata recordedEventMetadata
    );

    @NotNull void updateCompanyName(
        @NotNull JobApplicationId id, @NotBlank String name, @NotNull RecordedEventMetadata recordedEventMetadata
    );

    @NotNull void updateStatus(
        @NotNull JobApplicationId id, @NotNull JobApplicationStatus status, @NotNull RecordedEventMetadata recordedEventMetadata
    );

    @NotNull void updateContent(
        @NotNull JobApplicationId id, @NotBlank String content, @NotNull RecordedEventMetadata recordedEventMetadata
    );
}
