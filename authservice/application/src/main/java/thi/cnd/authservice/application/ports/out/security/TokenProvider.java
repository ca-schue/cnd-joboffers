package thi.cnd.authservice.application.ports.out.security;

import thi.cnd.authservice.domain.model.account.Account;
import thi.cnd.authservice.domain.model.account.AccountAccessToken;
import thi.cnd.authservice.domain.model.client.Client;
import thi.cnd.authservice.domain.model.client.ClientAccessToken;

public interface TokenProvider {
    public AccountAccessToken createAccountAccessToken(Account account);
    public ClientAccessToken createClientAccessToken(Client client);
}
