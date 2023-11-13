package thi.cnd.userservice.domain.model.user;


public record UserSettings(
        boolean nightModeActive
) {

    public UserSettings() {
        this(false);
    }

}
