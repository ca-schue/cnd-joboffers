package thi.cnd.authservice.core.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JwtConstants {
    public static final String ACCOUNT = "account";
    public static final String CLIENT = "client";

    public static final String SUBJECT_TYPE_CLAIM_NAME = "subject-type";
    public static final String SCOPE_TYPE_CLAIM_NAME = "scope";

}
