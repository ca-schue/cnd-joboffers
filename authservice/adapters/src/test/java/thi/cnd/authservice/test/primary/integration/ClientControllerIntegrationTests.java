package thi.cnd.authservice.test.primary.integration;

public class ClientControllerIntegrationTests {
    // TODO: Test login methods simply on controller-level?

    // TODO: createNewClient() check if:
    //  - accessible without Authentication header
    //  - valid (non-empty) ClientCreationRequestDTO:
    //      > new non-existing Client: mock ClientService "createNewClient()" / check MongoDB + clientWithPlaintextPassword DTO returned with HTTP 200
    //      > conflicting Client id: assertThrows: ClientAlreadyExistsException / HttpStatus.CONFLICT
    //  - invalid (empty) ClientCreationRequestDTO:
    //      > assertThrows: ... / HttpStatus....


    // TODO: loginClient() check if:
    //  - HTTP_GET /oauth2/token?grant_type=client_credentials invokes CustomRegisteredClientRepository
    //  - CustomRegisteredClientRepository: check if correct Client is loaded (id matches) + findClientByNameAndUpdateLastLogin is called (mocked? if not, check if lastLogin is updated in DB)
    //  - valid client credentials:
    //      > loginClient() is called + access token minted (verify jwt or rely on jwt provider unit tests?) + HTTP.Status = 200
    //  - invalid client credentials:
    //      > loginClient() is not called + HTTP.NOT_FOUND (?)


    // TODO: deleteClient()
    //  - only invoked with valid JWT (subject-type=client or account)
    //  - client name exists:
    //      > service method called / client deleted from DB / HTTP_STATUS.NO_CONTENT
    //  - client name does not exist:
    //      > ClientNotFoundByNameException / HTTP_STATUS.NOT_FOUND


    // TODO: resetPassword()
    //  - only invoked with valid JWT with clientId = Query client id
    //  - client id exists:
    //      > check if service method invoked / new password in mongo DB
    //      > client DTO with new password returned + HTTP 200
    //  - client id not exists:
    //      > ClientNotFoundByNameException + HTTP CONFLICT
}
