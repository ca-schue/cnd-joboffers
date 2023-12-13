package thi.cnd.authservice.adapters.in.security.basicAuthAccountLogin;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import thi.cnd.authservice.domain.model.account.InternalAccount;

import java.util.Collection;
import java.util.Set;

public record InternalAccountDetails(InternalAccount internalAccount) implements UserDetails {

    public String getId() {
        return internalAccount.getId().toString();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of();
    }

    @Override
    public String getPassword() {
        return internalAccount.getEncryptedPassword();
    }

    @Override
    public String getUsername() {
        return getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
