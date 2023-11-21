package thi.cnd.authservice.adapters.in.security.authentication.loginAuthentication.oidcAccount;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import thi.cnd.authservice.application.ports.out.repository.AccountRepository;
import thi.cnd.authservice.domain.exceptions.AccountNotFoundBySubjectException;
import thi.cnd.authservice.domain.model.account.OidcAccount;

@Service
@AllArgsConstructor
public class AuthenticatedOidcIdTokenService {

    private final AccountRepository accountPort;
    public AuthenticatedOidcIdToken loadIdTokenBySubject(String subject) {
        try {
            OidcAccount oidcAccount = accountPort.findOidcAccountBySubjectAndUpdateLastLogin(subject);
            return new AuthenticatedOidcIdToken(subject, oidcAccount);
        } catch (AccountNotFoundBySubjectException e) {
            return new AuthenticatedOidcIdToken(subject, null);
        }
    }
}
