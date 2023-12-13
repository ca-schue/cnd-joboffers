package thi.cnd.authservice.adapters.out.security.jwt;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;


@Validated
@Getter
@Component("JwtClaimsAdapterOut")
class JwtClaims {

    @NotBlank public static String subjectTypeAccount;

    @Value("${jwt-claims.subjectTypeAccount}")
    public void setSubjectTypeAccount(String subjectTypeAccount) {
        JwtClaims.subjectTypeAccount = subjectTypeAccount;
    }

    @NotBlank public static String subjectTypeClient;

    @Value("${jwt-claims.subjectTypeClient}")
    public void setSubjectTypeClient(String subjectTypeClient) {
        JwtClaims.subjectTypeClient = subjectTypeClient;
    }


    @NotBlank public static String subjectTypeClaimName;

    @Value("${jwt-claims.subjectTypeClaimName}")
    public void setSubjectTypeClaimName(String subjectTypeClaimName) {
        JwtClaims.subjectTypeClaimName = subjectTypeClaimName;
    }


    @NotBlank public static String verifiedClaimName;

    @Value("${jwt-claims.verifiedClaimName}")
    public void setVerifiedClaimName(String verifiedClaimName) {
        JwtClaims.verifiedClaimName = verifiedClaimName;
    }


    @NotBlank public static String subjectClaimName;

    @Value("${jwt-claims.subjectClaimName}")
    public void setSubjectClaimName(String subjectClaimName) {
        JwtClaims.subjectClaimName = subjectClaimName;
    }


    @NotBlank public static String scopeClaimName;

    @Value("${jwt-claims.scopeClaimName}")
    public void setScopeClaimName(String scopeClaimName) {
        JwtClaims.scopeClaimName = scopeClaimName;
    }

}
