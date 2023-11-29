package thi.cnd.userservice.adapters.in.rest.company;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import thi.cnd.userservice.adapters.generated.rest.model.*;
import thi.cnd.userservice.domain.model.company.*;
import thi.cnd.userservice.domain.model.user.*;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CompanyDtoMapper {

    CompanyDTO toCompanyDTO(Company company);

    CompanyDetails toCompanyDetails(CompanyDetailsDTO dto);

    CompanyLinks toCompanyLinks(CompanyLinksDTO dto);

    @BeanMapping(ignoreUnmappedSourceProperties = { "owner", "members", "partnerProgram" })
    PublicCompanyProfileDTO toPublicProfileDTO(Company company);

    private <T> PaginationDTO toPaginationDTO(Page<T> page) {
        return new PaginationDTO()
                .totalItemCount(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .currentPage(page.getNumber() + 1)
                .itemCount(page.getNumberOfElements());
    }

    default PaginatedPublicCompanyProfileResponseDTO toPaginatedCompaniesDTO(Page<Company> page) {
        return new PaginatedPublicCompanyProfileResponseDTO()
                .pagination(toPaginationDTO(page))
                .content(page.map(this::toPublicProfileDTO).getContent());
    }

    default UUID toUUID(CompanyId companyId) { return companyId.getId(); }

    default UUID toUUID(UserId userId) {
        return userId.getId();
    }

}
