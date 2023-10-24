package thi.cnd.authservice.test.primary.integration;

public class EndpointAuthorizationIntegrationTests {
    // TODO: Check with JWTs or mocked SecurityContextHolder?
    // TODO: = Integration Tests for Authorization Managers
    // TODO: AuthenticationTypeAuthorizationManager: Group Endpoints by subject-type. For all endpoints: Check if JWT with subject-type != allowed subject-type are declied
    // TODO: IdMatcherAuthorizationManager: Check if endpoints with ID query parameters are only accessible with correct id
    // TODO: AnonymousAuthorizationManager: check if permitAll() endpoints are accessible with and without Authentication header
}
