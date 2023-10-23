package thi.cnd.userservice.core.port.secondary.event;

import jakarta.validation.constraints.NotNull;

public interface Event {

    @NotNull EventTopics getTopic();

}