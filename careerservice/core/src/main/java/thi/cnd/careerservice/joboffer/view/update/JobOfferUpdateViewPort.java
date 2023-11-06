package thi.cnd.careerservice.joboffer.view.update;

import org.springframework.validation.annotation.Validated;

import thi.cnd.careerservice.joboffer.model.JobOfferId;
import thi.cnd.careerservice.joboffer.view.model.JobOfferView;
import jakarta.validation.constraints.NotNull;

@Validated
public interface JobOfferUpdateViewPort {
    @NotNull JobOfferView createNewJobOffer(@NotNull JobOfferView newView);

    @NotNull JobOfferView save(@NotNull JobOfferView jobOfferView);

    @NotNull JobOfferView deleteJobOffer(@NotNull JobOfferId id);
}
