package thi.cnd.authservice.primary.rest.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import thi.cnd.authservice.core.exceptions.AccountNotFoundByEmailException;
import thi.cnd.authservice.core.model.Account;
import thi.cnd.authservice.core.ports.secondary.AccountRepositoryPort;

@Service
@AllArgsConstructor
public class AccountDetailsService implements UserDetailsService {

    private final AccountRepositoryPort accountPort;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            Account account = accountPort.findAccountByEmailAndUpdateLastLogin(email);
            return new AccountDetails(account);
        } catch(AccountNotFoundByEmailException e) {
            throw new UsernameNotFoundException("Could not find account with email " + email);
        }
    }
}
