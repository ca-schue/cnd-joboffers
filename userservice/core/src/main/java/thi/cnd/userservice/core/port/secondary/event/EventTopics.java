package thi.cnd.userservice.core.port.secondary.event;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EventTopics {

    USER_REGISTERED("user-registered"),
    USER_DELETED("user-deleted"),
    COMPANY_REGISTERED("company-registered"),
    USER_INVITED_TO_COMPANY("user-invited-to-company"),
    COMPANY_NAME_CHANGED("company-name-changed"),
    COMPANY_DELETED("company-deleted");

    public static final String USER_SERVICE_PREFIX = "user-service";

    private final String suffix;

    public String getName() {
        return USER_SERVICE_PREFIX + "_" + suffix;
    }

}