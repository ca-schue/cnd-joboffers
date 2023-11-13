package thi.cnd.userservice.domain.exceptions;

import thi.cnd.userservice.domain.model.company.CompanyId;

public class CompanyNotFoundByIdException extends Exception {
    public CompanyNotFoundByIdException(CompanyId companyId) {
        super("Company " + companyId.toString() + " was not found.");
    }
}
