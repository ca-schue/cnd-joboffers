package thi.cnd.authservice.secondary.repository.account.model;


import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import thi.cnd.authservice.core.model.account.Account;
import thi.cnd.authservice.core.model.account.InternalAccount;
import thi.cnd.authservice.core.model.account.OidcAccount;
import thi.cnd.authservice.core.model.account.AccountProvider;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AccountDAOMapper {

    default Account toAccount(AccountDAO accountDao) {
        return switch (AccountProvider.valueOf(accountDao.getProvider())) {
            case OIDC -> toOidcAccount((OidcAccountDAO) accountDao);
            case INTERNAL -> toInternalAccount((InternalAccountDAO) accountDao);
        };
    }

    default AccountDAO toAccountDAO(Account account) {
        return switch (account.getProvider()) {
            case OIDC -> toOidcAccountDAO((OidcAccount) account);
            case INTERNAL -> toInternalAccountDAO((InternalAccount) account);
        };
    }

    InternalAccount toInternalAccount(InternalAccountDAO internalAccountDAO);
    InternalAccountDAO toInternalAccountDAO(InternalAccount internalAccount);

    OidcAccount toOidcAccount(OidcAccountDAO oidcAccountDAO);
    OidcAccountDAO toOidcAccountDAO(OidcAccount oidcAccount);
}
