package thi.cnd.careerservice.joboffer.query.domain.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import thi.cnd.careerservice.company.model.CompanyId;

public record AvailableJobOfferSearchFilter(
    Optional<String> title,
    List<CompanyId> companyIds,
    Pageable pageable
) {}
