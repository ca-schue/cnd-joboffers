package thi.cnd.careerservice.joboffer.model;

import javax.money.MonetaryAmount;

public record SalaryRange(
    MonetaryAmount lowerBound,
    MonetaryAmount upperBound
) {
}
