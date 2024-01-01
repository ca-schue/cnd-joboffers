package thi.cnd.authservice.adapters.in.http.oidcAccountLogin;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import thi.cnd.authservice.domain.AccountService;
import thi.cnd.authservice.domain.exceptions.AccountNotFoundBySubjectException;
import thi.cnd.authservice.domain.model.account.OidcAccount;

@Service
@AllArgsConstructor
class AuthenticatedOidcIdTokenService {

    private final AccountService accountService;
    public AuthenticatedOidcIdToken loadIdTokenBySubject(String subject) {
        try {
            OidcAccount oidcAccount = accountService.updateLastOidcAccountLogin(subject);
            return new AuthenticatedOidcIdToken(subject, oidcAccount);
        } catch (AccountNotFoundBySubjectException e) {
            return new AuthenticatedOidcIdToken(subject, null);
        }
    }
}
