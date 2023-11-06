package thi.cnd.careerservice.jobapplication.model;

import java.util.Objects;
import java.util.UUID;

import thi.cnd.careerservice.shared.model.BaseUUID;
import thi.cnd.careerservice.joboffer.model.JobOfferId;
import thi.cnd.careerservice.user.model.UserId;

public class JobApplicationId extends BaseUUID {
    public JobApplicationId() {
        super();
    }

    public JobApplicationId(UUID id) {
        super(id);
    }

    public JobApplicationId(JobOfferId jobOfferId, UserId userId) {
        super(UUID.nameUUIDFromBytes(
            String.valueOf(Objects.hash(jobOfferId.getId(), userId.getId())).getBytes()
        ));
    }
}
