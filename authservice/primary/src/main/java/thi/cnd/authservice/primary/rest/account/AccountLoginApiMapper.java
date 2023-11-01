package thi.cnd.authservice.primary.rest.account;

import org.mapstruct.*;
import thi.cnd.authservice.api.generated.model.AccountDTO;
import thi.cnd.authservice.api.generated.model.AccountLoginResponseDTO;
import thi.cnd.authservice.api.generated.model.InternalAccountDTO;
import thi.cnd.authservice.api.generated.model.OidcAccountDTO;
import thi.cnd.authservice.core.model.account.*;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AccountLoginApiMapper {

    default AccountDTO toAccountDTO(Account account) {
        return switch (account.getProvider()) {
            case OIDC -> toOidcDTO((OidcAccount) account);
            case INTERNAL -> toInternalDTO((InternalAccount) account);
        };
    }

    @Mapping(source = "provider", target = "accountType")
    @BeanMapping(ignoreUnmappedSourceProperties = { "encryptedPassword", "lastLogin", "verified" })
    InternalAccountDTO toInternalDTO(InternalAccount internalAccount);

    @Mapping(source = "provider", target = "accountType")
    @BeanMapping(ignoreUnmappedSourceProperties = { "lastLogin", "verified" })
    OidcAccountDTO toOidcDTO(OidcAccount oidcAccount);

    default AccountLoginResponseDTO toLoginResponseDTO(AccountDTO accountDTO, AccountAccessToken token) {
        return new AccountLoginResponseDTO(accountDTO, token.signedAccountJwt().getTokenValue());
    }

    default UUID toUUID(AccountId accountId) {
        return accountId.id();
    }
}
