package thi.cnd.authservice.primary.rest.model;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import thi.cnd.authservice.api.generated.model.AccessTokenResponseDTO;
import thi.cnd.authservice.api.generated.model.AccountDTO;
import thi.cnd.authservice.core.model.Account;
import thi.cnd.authservice.core.model.AccountAccessToken;
import thi.cnd.authservice.core.model.AccountId;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AccountLoginApiMapper {


    @Mapping(target = "email", source = "email")
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "provider", source = "provider")
    @BeanMapping(ignoreUnmappedSourceProperties = { "encryptedPassword", "lastLogin" })
    AccountDTO toDTO(Account account);

    default AccessTokenResponseDTO toDTO(AccountAccessToken token) {
        return new AccessTokenResponseDTO(token.accessToken());
        // TODO: Valid until including? -> Info in JWT
    }

    default UUID toUUID(AccountId accountId) {
        return accountId.id();
    }
}
