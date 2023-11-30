package thi.cnd.careerservice.http;

import java.math.BigDecimal;
import java.util.UUID;

import javax.money.Monetary;
import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;
import org.springframework.data.domain.Page;

import thi.cnd.careerservice.api.generated.model.MoneyDTO;
import thi.cnd.careerservice.api.generated.model.PaginationDTO;
import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationId;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferId;
import thi.cnd.careerservice.user.model.UserId;

/**
 * Provide mapping functionality for shared objects
 */
public class CommonDTOMapper {

    private CommonDTOMapper() {}

    public static <T> PaginationDTO toPaginationDTO(Page<T> page) {
        return new PaginationDTO()
            .totalItemCount(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .currentPage(page.getNumber())
            .itemCount(page.getNumberOfElements());
    }

    public static UUID toUUID(JobOfferId jobOfferId) {
        return jobOfferId.getId();
    }

    public static UUID toUUID(JobApplicationId jobApplicationId) {
        return jobApplicationId.getId();
    }

    public static UUID toUUID(CompanyId companyId) {
        return companyId.getId();
    }

    public static UUID toUUID(UserId userId) {
        return userId.getId();
    }

    public static CompanyId toCompanyId(UUID companyId) {
        return new CompanyId(companyId);
    }

    public static JobOfferId toJobOfferId(UUID jobOfferId) {
        return new JobOfferId(jobOfferId);
    }

    public static UserId toUserId(UUID userId) {
        return new UserId(userId);
    }

    public static MonetaryAmount toMoney(MoneyDTO moneyDTO) {
        return Money.of(BigDecimal.valueOf(moneyDTO.getAmount()), Monetary.getCurrency(moneyDTO.getCurrency()));
    }

    public static MoneyDTO toDto(MonetaryAmount amount) {
        return new MoneyDTO(amount.getNumber().floatValue(), amount.getCurrency().getCurrencyCode());
    }

}
