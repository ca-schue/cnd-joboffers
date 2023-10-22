package thi.cnd.authservice.secondary.repository.account.model;


import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import thi.cnd.authservice.core.model.account.Account;
import thi.cnd.authservice.core.model.account.InternalAccount;
import thi.cnd.authservice.core.model.account.OidcAccount;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AccountDAOMapper {

    Account toAccount(AccountDAO accountDao);
    AccountDAO toAccountDAO(Account account);

    InternalAccount toInternalAccount(InternalAccountDAO internalAccountDAO);
    InternalAccountDAO toInternalAccountDAO(InternalAccount internalAccount);

    OidcAccount toOidcAccount(OidcAccountDAO oidcAccountDAO);
    OidcAccountDAO toOidcAccountDAO(OidcAccount oidcAccount);
}
