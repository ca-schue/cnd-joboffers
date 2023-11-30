package thi.cnd.careerservice.joboffer.command.domain.model;

import javax.money.MonetaryAmount;

public record SalaryRange(
    MonetaryAmount lowerBound,
    MonetaryAmount upperBound
) {
}
