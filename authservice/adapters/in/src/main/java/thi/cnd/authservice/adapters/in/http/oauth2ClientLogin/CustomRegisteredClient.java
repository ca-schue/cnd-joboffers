package thi.cnd.authservice.adapters.in.http.oauth2ClientLogin;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.validation.annotation.Validated;
import thi.cnd.authservice.domain.model.client.Client;

import java.time.Instant;
import java.util.Collections;
import java.util.Set;

@Getter
@Validated
class CustomRegisteredClient extends RegisteredClient {

    private final RegisteredClient registeredClient;
    private final Set<String> audiences;
    @NotNull private final Client client;

    public CustomRegisteredClient(@NotNull Client client) {
        this.registeredClient = RegisteredClient
                .withId(client.name())
                .clientId(client.name())
                .clientSecret(client.encryptedPassword())
                .scopes(scopes -> scopes.addAll(client.scopes() == null ? Collections.emptySet() : client.scopes()))
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .build();
        this.audiences = client.audiences();
        this.client = client;
    }

    @Override
    public String getId() {
        return registeredClient.getId();
    }

    public String getClientId() {
        return registeredClient.getClientId();
    }


    public Instant getClientIdIssuedAt() {
        return registeredClient.getClientIdIssuedAt();
    }

    public String getClientSecret() {
        return registeredClient.getClientSecret();
    }

    public Instant getClientSecretExpiresAt() {
        return registeredClient.getClientSecretExpiresAt();
    }

    public String getClientName() {
        return registeredClient.getClientName();
    }

    public Set<ClientAuthenticationMethod> getClientAuthenticationMethods() {
        return registeredClient.getClientAuthenticationMethods();
    }

    public Set<AuthorizationGrantType> getAuthorizationGrantTypes() {
        return registeredClient.getAuthorizationGrantTypes();
    }

    public Set<String> getRedirectUris() {
        return registeredClient.getRedirectUris();
    }

    public Set<String> getPostLogoutRedirectUris() {
        return registeredClient.getPostLogoutRedirectUris();
    }

    public Set<String> getScopes() {
        return registeredClient.getScopes();
    }

    public ClientSettings getClientSettings() {
        return registeredClient.getClientSettings();
    }

    public TokenSettings getTokenSettings() {
        return registeredClient.getTokenSettings();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CustomRegisteredClient) {
            return registeredClient.equals(obj) && this.audiences.equals(((CustomRegisteredClient) obj).getAudiences());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() { return registeredClient.hashCode(); }

    @Override
    public String toString() { return registeredClient.toString(); }
}
