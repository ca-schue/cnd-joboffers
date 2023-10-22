package thi.cnd.authservice.primary.security.authentication.loginAuthentication.internalAccount;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import thi.cnd.authservice.core.exceptions.AccountNotFoundByEmailException;
import thi.cnd.authservice.core.model.account.Account;
import thi.cnd.authservice.core.model.account.InternalAccount;
import thi.cnd.authservice.core.ports.secondary.AccountRepositoryPort;

@Service
@AllArgsConstructor
public class InternalAccountDetailsService implements UserDetailsService {

    private final AccountRepositoryPort accountPort;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            InternalAccount internalAccount = accountPort.findInternalAccountByEmailAndUpdateLastLogin(email);
            return new InternalAccountDetails(internalAccount);
        } catch(AccountNotFoundByEmailException e) {
            throw new UsernameNotFoundException("Could not find internal account with email " + email);
        }
    }
}
