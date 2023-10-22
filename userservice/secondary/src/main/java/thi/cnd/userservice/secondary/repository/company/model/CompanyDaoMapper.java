package thi.cnd.userservice.secondary.repository.company.model;

import org.springframework.stereotype.Component;
import thi.cnd.userservice.core.model.company.Company;
import thi.cnd.userservice.core.model.company.CompanyDetails;
import thi.cnd.userservice.core.model.company.CompanyLinks;
import thi.cnd.userservice.core.model.company.CompanyPartnerProgram;

@Component
public class CompanyDaoMapper {

    public Company toCompany(CompanyDAO dao) {
        return new Company(
                dao.id(),
                dao.owner(),
                dao.members(),
               new CompanyDetails(
                   dao.details().name(),
                   dao.details().description(),
                   dao.details().tags(),
                   dao.details().location()
                ),
                new CompanyLinks(
                    dao.links().websiteUrl(),
                    dao.links().socialMedia()
                ),
               new CompanyPartnerProgram(
                   dao.partnerProgram().partnerUntil()
               )
        );
    }


    public CompanyDAO toDAO(Company company) {
        return new CompanyDAO(
                company.getId(),
                company.getOwner(),
                company.getMembers(),
                new CompanyDetailsDAO(
                    company.getDetails().name(),
                    company.getDetails().description(),
                    company.getDetails().tags(),
                    company.getDetails().location()
                ),
                new CompanyLinksDAO(
                    company.getLinks().websiteUrl(),
                    company.getLinks().socialMedia()
                ),
                new CompanyPartnerProgramDAO(
                company.getPartnerProgram().partnerUntil()
                )
        );
    }

}
