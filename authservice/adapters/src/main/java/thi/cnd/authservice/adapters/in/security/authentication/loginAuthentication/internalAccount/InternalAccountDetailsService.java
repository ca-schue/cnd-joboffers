package thi.cnd.authservice.adapters.in.security.authentication.loginAuthentication.internalAccount;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import thi.cnd.authservice.application.ports.out.repository.AccountRepository;
import thi.cnd.authservice.domain.exceptions.AccountNotFoundByEmailException;
import thi.cnd.authservice.domain.model.account.InternalAccount;

@Service
@AllArgsConstructor
public class InternalAccountDetailsService implements UserDetailsService {

    private final AccountRepository accountPort;

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
