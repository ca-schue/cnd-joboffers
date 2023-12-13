package thi.cnd.authservice.adapters.out.repository.accounts.DAOs;


import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import thi.cnd.authservice.adapters.out.repository.accounts.DAOs.AccountDAO;
import thi.cnd.authservice.adapters.out.repository.accounts.DAOs.InternalAccountDAO;
import thi.cnd.authservice.adapters.out.repository.accounts.DAOs.OidcAccountDAO;
import thi.cnd.authservice.domain.model.account.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AccountDaoMapper {

    default Account toAccount(AccountDAO accountDao) {
        return switch (AccountProvider.valueOf(accountDao.getProvider())) {
            case AccountProvider.OIDC -> toOidcAccount((OidcAccountDAO) accountDao);
            case AccountProvider.INTERNAL -> toInternalAccount((InternalAccountDAO) accountDao);
        };
    }

    default AccountDAO toAccountDAO(Account account) {
        return switch (account.getProvider()) {
            case AccountProvider.OIDC -> toOidcAccountDAO((OidcAccount) account);
            case AccountProvider.INTERNAL -> toInternalAccountDAO((InternalAccount) account);
        };
    }

    InternalAccount toInternalAccount(InternalAccountDAO internalAccountDAO);
    InternalAccountDAO toInternalAccountDAO(InternalAccount internalAccount);

    OidcAccount toOidcAccount(OidcAccountDAO oidcAccountDAO);
    OidcAccountDAO toOidcAccountDAO(OidcAccount oidcAccount);
}
