package thi.cnd.authservice.primary.rest.account;

import org.mapstruct.*;
import thi.cnd.authservice.api.generated.model.AccessTokenResponseDTO;
import thi.cnd.authservice.api.generated.model.AccountDTO;
import thi.cnd.authservice.api.generated.model.InternalAccountDTO;
import thi.cnd.authservice.api.generated.model.OidcAccountDTO;
import thi.cnd.authservice.core.model.account.*;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AccountLoginApiMapper {


    /*
    @Mapping(target = "id", source = "id")
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "provider", source = "provider")
    @BeanMapping(ignoreUnmappedSourceProperties = { "encryptedPassword", "lastLogin" })
    AccountDTO toDTO(Account account);*/

    @Mapping(target = "type", ignore = true)
    @BeanMapping(ignoreUnmappedSourceProperties = { "encryptedPassword", "lastLogin" })
    InternalAccountDTO toInternalDTO(InternalAccount internalAccount);

    @Mapping(target = "type", ignore = true)
    @BeanMapping(ignoreUnmappedSourceProperties = { "lastLogin" })
    OidcAccountDTO toOidcDTO(OidcAccount oidcAccount);

    default AccessTokenResponseDTO toDTO(AccountAccessToken token) {
        return new AccessTokenResponseDTO(token.accessToken());
        // TODO: Valid until including? -> Info in JWT
    }

    default UUID toUUID(AccountId accountId) {
        return accountId.id();
    }
}
