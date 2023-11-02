package thi.cnd.authservice.core.domain.jwt;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class JwtConstants {
    public static final String ACCOUNT = "account";
    public static final String CLIENT = "client";

    public static final String SUBJECT_TYPE_CLAIM_NAME = "subject-type";
    public static final String VERIFIED_CLAIM_NAME = "verified";
    public static final String SUBJECT_CLAIM_NAME = "sub";
    public static final String SCOPE_CLAIM_NAME = "scope";

}
