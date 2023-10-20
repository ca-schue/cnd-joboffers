/*
 * User-Service
 * API.
 *
 * The version of the OpenAPI document: 0.0.1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package org.openapitools.client.api;

import org.openapitools.client.ApiException;
import org.openapitools.client.model.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for AuthApi
 */
@Disabled
public class AuthApiTest {

    private final AuthApi api = new AuthApi();

    /**
     * Get the user data from the provided access token.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getRequestUserDataTest() throws ApiException {
        User response = api.getRequestUserData();
        // TODO: test validations
    }

}
