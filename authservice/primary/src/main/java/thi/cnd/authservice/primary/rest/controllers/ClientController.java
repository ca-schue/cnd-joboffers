package thi.cnd.authservice.primary.rest.controllers;
/*
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import thi.cnd.authservice.api.generated.ClientManagementApi;
import thi.cnd.authservice.api.generated.model.ClientCreationRequestDTO;
import thi.cnd.authservice.api.generated.model.ClientCreationResponseDTO;
import thi.cnd.authservice.core.exceptions.ClientAlreadyExistsException;
import thi.cnd.authservice.core.exceptions.ClientNotFoundByNameException;
import thi.cnd.authservice.core.model.ClientWithPlaintextPassword;
import thi.cnd.authservice.core.ports.primary.ClientServicePort;
import thi.cnd.authservice.primary.rest.model.ClientApiMapper;

import java.util.Set;

@RestController
@RequiredArgsConstructor
public class ClientController implements ClientManagementApi {

    private final ClientServicePort service;
    private final ClientApiMapper mapper;

    @Override
    public ResponseEntity<ClientCreationResponseDTO> createNewClient(ClientCreationRequestDTO requestDTO) {
        Set<String> audiences = requestDTO.getAudiences() != null ? requestDTO.getAudiences() : Set.of();
        try {
           ClientWithPlaintextPassword clientWithPlaintextPassword = service.createNewClient(requestDTO.getName(), audiences, requestDTO.getScopes());
            return ResponseEntity.ok(mapper.toDTO(clientWithPlaintextPassword));
        } catch (ClientAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ClientCreationResponseDTO> resetPassword(String clientId) {
        try {
            ClientWithPlaintextPassword clientWithNewPlaintextPassword = service.setNewRandomPassword(clientId);
            return ResponseEntity.ok(mapper.toDTO(clientWithNewPlaintextPassword));
        } catch (ClientNotFoundByNameException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

}*/
