package thi.cnd.authservice.adapters.in.http.account;

import org.mapstruct.*;
import org.springframework.security.oauth2.jwt.Jwt;
import thi.cnd.authservice.api.generated.model.AccountDTO;
import thi.cnd.authservice.api.generated.model.AccountLoginResponseDTO;
import thi.cnd.authservice.api.generated.model.InternalAccountDTO;
import thi.cnd.authservice.api.generated.model.OidcAccountDTO;
import thi.cnd.authservice.domain.model.account.*;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
interface AccountDtoMapper {

    default AccountDTO toAccountDTO(Account account) {
        return switch (account.getProvider()) {
            case AccountProvider.OIDC -> toOidcDTO((OidcAccount) account);
            case AccountProvider.INTERNAL -> toInternalDTO((InternalAccount) account);
        };
    }

    @Mapping(source = "provider", target = "accountType")
    @BeanMapping(ignoreUnmappedSourceProperties = { "encryptedPassword", "lastLogin", "verified" })
    InternalAccountDTO toInternalDTO(InternalAccount internalAccount);

    @Mapping(source = "provider", target = "accountType")
    @BeanMapping(ignoreUnmappedSourceProperties = { "lastLogin", "verified" })
    OidcAccountDTO toOidcDTO(OidcAccount oidcAccount);

    default AccountLoginResponseDTO toLoginResponseDTO(AccountDTO accountDTO, Jwt accountJwt) {
        return new AccountLoginResponseDTO(accountDTO, accountJwt.getTokenValue());
    }

    default UUID toUUID(AccountId accountId) {
        return accountId.id();
    }
}
