package thi.cnd.careerservice.company.model;

import java.util.UUID;

import thi.cnd.careerservice.shared.model.BaseUUID;

public class CompanyId extends BaseUUID {

    public CompanyId() {
        super();
    }

    public CompanyId(UUID id) {
        super(id);
    }

    public CompanyId(String id) {
        super(UUID.fromString(id));
    }

}
