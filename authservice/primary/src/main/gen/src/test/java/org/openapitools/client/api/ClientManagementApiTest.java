/*
 * Auth-Service
 * The api for the auth-service. This service creates users and clients to authenticate against the internal applications of this project. 
 *
 * The version of the OpenAPI document: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package org.openapitools.client.api;

import org.openapitools.client.ApiException;
import org.openapitools.client.model.ClientCreationRequest;
import org.openapitools.client.model.ClientCreationResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for ClientManagementApi
 */
@Disabled
public class ClientManagementApiTest {

    private final ClientManagementApi api = new ClientManagementApi();

    /**
     * Creates a new client.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void createNewClientTest() throws ApiException {
        ClientCreationRequest clientCreationRequest = null;
        ClientCreationResponse response = api.createNewClient(clientCreationRequest);
        // TODO: test validations
    }

    /**
     * Resets the password to a new random one. {clientId} must match client id in access token
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void resetPasswordTest() throws ApiException {
        String clientId = null;
        ClientCreationResponse response = api.resetPassword(clientId);
        // TODO: test validations
    }

}