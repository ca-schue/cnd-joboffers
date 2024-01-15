package thi.cnd.authservice.adapters.in.http.client;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import thi.cnd.authservice.adapters.in.http.HttpErrorException;
import thi.cnd.authservice.api.generated.ClientManagementApi;
import thi.cnd.authservice.api.generated.model.ClientCreationRequestDTO;
import thi.cnd.authservice.api.generated.model.ClientCreationResponseDTO;
import thi.cnd.authservice.domain.ClientService;
import thi.cnd.authservice.domain.exceptions.ClientAlreadyExistsException;
import thi.cnd.authservice.domain.exceptions.ClientNotFoundByNameException;
import thi.cnd.authservice.domain.model.client.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
class ClientHttpControllerImpl implements ClientManagementApi {

    private final ClientService service;
    private final ClientDtoMapper mapper;

    @Override
    public ResponseEntity<ClientCreationResponseDTO> createNewClient(ClientCreationRequestDTO requestDTO) {
        Set<String> audiences = requestDTO.getAudiences() != null ? requestDTO.getAudiences() : Set.of();
        try {
           ClientWithPlaintextPassword clientWithPlaintextPassword = service.createNewClient(requestDTO.getName(), audiences, requestDTO.getScopes());
            return ResponseEntity.ok(mapper.toDTO(clientWithPlaintextPassword));
        } catch (ClientAlreadyExistsException e) {
            throw new HttpErrorException(HttpStatus.CONFLICT, e.getMessage());
        } catch (ConstraintViolationException e4) {
            throw new HttpErrorException(HttpStatus.BAD_REQUEST, "Please enter valid inputs.");
        }
    }

    @Override
    public ResponseEntity<Void> deleteClient(String name) {
        try {
            service.deleteClient(name);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ClientNotFoundByNameException e) {
            throw new HttpErrorException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ClientCreationResponseDTO> resetPassword(String clientId) {
        try {
            ClientWithPlaintextPassword clientWithNewPlaintextPassword = service.setNewRandomPassword(clientId);
            return ResponseEntity.ok(mapper.toDTO(clientWithNewPlaintextPassword));
        } catch (ClientNotFoundByNameException e) {
            throw new HttpErrorException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

}
