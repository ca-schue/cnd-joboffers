package thi.cnd.careerservice.joboffer.domain;

import thi.cnd.careerservice.company.model.CompanyId;

/**
 * Applies external events outside the domain to affected job offers
 */
public interface JobOfferExternalEventHandler {
    /**
     * Called when a company was deleted
     */
    void companyDeleted(CompanyId companyId);
}
