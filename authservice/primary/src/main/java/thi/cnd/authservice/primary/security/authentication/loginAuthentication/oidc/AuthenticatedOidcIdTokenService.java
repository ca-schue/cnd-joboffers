package thi.cnd.authservice.primary.security.authentication.loginAuthentication.oidc;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import thi.cnd.authservice.core.exceptions.AccountNotFoundBySubjectException;
import thi.cnd.authservice.core.model.account.OidcAccount;
import thi.cnd.authservice.core.ports.secondary.AccountRepositoryPort;

@Service
@AllArgsConstructor
public class AuthenticatedOidcIdTokenService {

    private final AccountRepositoryPort accountPort;
    public AuthenticatedOidcIdToken loadIdTokenBySubject(String subject) {
        try {
            OidcAccount oidcAccount = accountPort.findOidcAccountBySubjectAndUpdateLastLogin(subject);
            return new AuthenticatedOidcIdToken(subject, oidcAccount);
        } catch (AccountNotFoundBySubjectException e) {
            return new AuthenticatedOidcIdToken(subject, null);
        }
    }
}
