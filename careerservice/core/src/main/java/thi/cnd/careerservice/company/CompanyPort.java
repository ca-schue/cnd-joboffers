package thi.cnd.careerservice.company;

import org.springframework.validation.annotation.Validated;

import thi.cnd.careerservice.company.model.Company;
import thi.cnd.careerservice.company.model.CompanyId;
import jakarta.validation.constraints.NotNull;

/**
 * Port that calls the user-service to retrieve company data
 */
@Validated
public interface CompanyPort {
    @NotNull Company getCompany(@NotNull CompanyId companyId);
}
