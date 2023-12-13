package thi.cnd.authservice.application.ports.out.security;

import thi.cnd.authservice.domain.model.AccessToken;
import thi.cnd.authservice.domain.model.account.Account;
import thi.cnd.authservice.domain.model.client.Client;

public interface TokenProvider {
    public AccessToken createAccountAccessToken(Account account);
    public AccessToken createClientAccessToken(Client client);
}
