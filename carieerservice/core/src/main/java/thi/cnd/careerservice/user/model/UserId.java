package thi.cnd.careerservice.user.model;

import java.util.UUID;

import thi.cnd.careerservice.shared.model.BaseUUID;

public class UserId extends BaseUUID {

    public UserId() {
        super();
    }

    public UserId(UUID id) {
        super(id);
    }

}
