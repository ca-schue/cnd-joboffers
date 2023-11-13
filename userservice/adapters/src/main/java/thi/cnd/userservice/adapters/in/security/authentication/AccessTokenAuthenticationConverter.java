package thi.cnd.userservice.adapters.in.security.authentication;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import thi.cnd.userservice.adapters.in.security.SecurityConstants;

public class AccessTokenAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        String subjectType = source.getClaimAsString(SecurityConstants.SUBJECT_TYPE_CLAIM_NAME);
        if (subjectType.equals(SecurityConstants.ACCOUNT)) {
            return new AuthenticatedAccount(source);
        } else if (subjectType.equals(SecurityConstants.CLIENT)) {
            return new AuthenticatedClient(source);
        } else {
            throw new RuntimeException("Unsupported subject type " + subjectType);
        }
    }
}
