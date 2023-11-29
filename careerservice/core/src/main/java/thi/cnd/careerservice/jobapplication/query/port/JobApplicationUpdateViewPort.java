package thi.cnd.careerservice.jobapplication.query.port;

import org.springframework.validation.annotation.Validated;

import thi.cnd.careerservice.shared.event.model.RecordedEventMetadata;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationId;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationStatus;
import thi.cnd.careerservice.jobapplication.query.domain.model.JobApplicationView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Validated
public interface JobApplicationUpdateViewPort {

    void createNewJobApplication(@NotNull JobApplicationView jobApplicationView);

    void deleteJobApplication(@NotNull JobApplicationId id);

    void updateJobOfferData(
        @NotNull JobApplicationId id, @NotBlank String title, @NotNull RecordedEventMetadata recordedEventMetadata
    );

    void updateCompanyName(
        @NotNull JobApplicationId id, @NotBlank String name, @NotNull RecordedEventMetadata recordedEventMetadata
    );

    void updateStatus(
        @NotNull JobApplicationId id, @NotNull JobApplicationStatus status, @NotNull RecordedEventMetadata recordedEventMetadata
    );

    void updateContent(
        @NotNull JobApplicationId id, @NotBlank String content, @NotNull RecordedEventMetadata recordedEventMetadata
    );
}
