package thi.cnd.userservice.core.model.user;

import lombok.AllArgsConstructor;


public record UserSettings(
        boolean nightModeActive
) {

    public UserSettings() {
        this(false);
    }

}
