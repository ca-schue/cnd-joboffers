package thi.cnd.authservice.secondary.repository.mongodb.account.model;


import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import thi.cnd.authservice.core.model.Account;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AccountDAOMapper {

    Account toAccount(AccountDAO accountDao);

    AccountDAO toDAO(Account account);
}
