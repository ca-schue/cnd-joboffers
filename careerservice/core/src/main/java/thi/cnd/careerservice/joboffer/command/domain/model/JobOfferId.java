package thi.cnd.careerservice.joboffer.command.domain.model;

import java.util.UUID;

import thi.cnd.careerservice.shared.model.BaseUUID;

public class JobOfferId extends BaseUUID {
    public JobOfferId() {
        super();
    }

    public JobOfferId(UUID id) {
        super(id);
    }
}
