package thi.cnd.careerservice.joboffer.query.port;

import org.springframework.validation.annotation.Validated;

import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferId;
import thi.cnd.careerservice.joboffer.query.domain.model.JobOfferView;
import jakarta.validation.constraints.NotNull;

@Validated
public interface JobOfferUpdateViewPort {
    @NotNull JobOfferView createNewJobOffer(@NotNull JobOfferView newView);

    @NotNull JobOfferView save(@NotNull JobOfferView jobOfferView);

    @NotNull JobOfferView deleteJobOffer(@NotNull JobOfferId id);
}
