package thi.cnd.authservice.secondary.repository.mongodb.clients.model;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import thi.cnd.authservice.core.model.Client;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ClientDaoMapper {

    Client toClient(ClientDAO dao);

    ClientDAO toDAO(Client client);

}
