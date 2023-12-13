package thi.cnd.userservice.adapters.in.jwtEndpointAuthentication;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;

class AccessTokenAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        String subjectType = source.getClaimAsString(JwtClaims.subjectTypeClaimName);
        if (subjectType.equals(JwtClaims.subjectTypeAccount)) {
            return new AuthenticatedAccount(source);
        } else if (subjectType.equals(JwtClaims.subjectTypeClient)) {
            return new AuthenticatedClient(source);
        } else {
            throw new RuntimeException("Unsupported subject type " + subjectType);
        }
    }
}
