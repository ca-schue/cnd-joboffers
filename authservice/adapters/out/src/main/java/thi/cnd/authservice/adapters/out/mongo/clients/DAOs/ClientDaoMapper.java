package thi.cnd.authservice.adapters.out.mongo.clients.DAOs;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import thi.cnd.authservice.domain.model.client.Client;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ClientDaoMapper {

    Client toClient(ClientDAO dao);

    ClientDAO toDAO(Client client);

}
