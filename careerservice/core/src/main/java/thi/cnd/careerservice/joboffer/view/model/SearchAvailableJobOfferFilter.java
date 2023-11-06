package thi.cnd.careerservice.joboffer.view.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import thi.cnd.careerservice.company.model.CompanyId;

public record SearchAvailableJobOfferFilter(
    Optional<String> title,
    List<CompanyId> companyIds,
    Pageable pageable
) {}
