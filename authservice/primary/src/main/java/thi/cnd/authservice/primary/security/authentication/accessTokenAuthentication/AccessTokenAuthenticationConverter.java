package thi.cnd.authservice.primary.security.authentication.accessTokenAuthentication;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import thi.cnd.authservice.core.domain.jwt.JwtConstants;

public class AccessTokenAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        String subjectType = source.getClaimAsString(JwtConstants.SUBJECT_TYPE_CLAIM_NAME);
        if (subjectType.equals(JwtConstants.ACCOUNT)) {
            return new AuthenticatedAccount(source);
        } else if (subjectType.equals(JwtConstants.CLIENT)) {
            return new AuthenticatedClient(source);
        } else {
            throw new RuntimeException("Unsupported subject type " + subjectType);
        }
    }
}
