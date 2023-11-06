package thi.cnd.careerservice.shared.model;

import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public abstract class BaseUUID {
    private UUID id;

    protected BaseUUID() {
        this.id = UUID.randomUUID();
    }

    protected BaseUUID(UUID id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id.toString();
    }

}
