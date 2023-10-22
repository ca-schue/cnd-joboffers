package thi.cnd.userservice.core.exception;

import thi.cnd.userservice.core.model.company.CompanyId;

public class CompanyNotFoundByIdException extends Exception {
    public CompanyNotFoundByIdException(CompanyId companyId) {
        super("Company " + companyId.toString() + " was not found.");
    }
}
