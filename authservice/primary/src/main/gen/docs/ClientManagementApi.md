# ClientManagementApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createNewClient**](ClientManagementApi.md#createNewClient) | **PUT** /clients/create |  |
| [**resetPassword**](ClientManagementApi.md#resetPassword) | **POST** /clients/{clientId}/reset-password |  |


<a name="createNewClient"></a>
# **createNewClient**
> ClientCreationResponse createNewClient(clientCreationRequest)



Creates a new client.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ClientManagementApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    ClientManagementApi apiInstance = new ClientManagementApi(defaultClient);
    ClientCreationRequest clientCreationRequest = new ClientCreationRequest(); // ClientCreationRequest | 
    try {
      ClientCreationResponse result = apiInstance.createNewClient(clientCreationRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling ClientManagementApi#createNewClient");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **clientCreationRequest** | [**ClientCreationRequest**](ClientCreationRequest.md)|  | |

### Return type

[**ClientCreationResponse**](ClientCreationResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Client was successfully created. |  -  |

<a name="resetPassword"></a>
# **resetPassword**
> ClientCreationResponse resetPassword(clientId)



Resets the password to a new random one. {clientId} must match client id in access token

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ClientManagementApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");
    
    // Configure HTTP bearer authorization: clientAccessToken
    HttpBearerAuth clientAccessToken = (HttpBearerAuth) defaultClient.getAuthentication("clientAccessToken");
    clientAccessToken.setBearerToken("BEARER TOKEN");

    ClientManagementApi apiInstance = new ClientManagementApi(defaultClient);
    String clientId = "clientId_example"; // String | 
    try {
      ClientCreationResponse result = apiInstance.resetPassword(clientId);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling ClientManagementApi#resetPassword");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **clientId** | **String**|  | |

### Return type

[**ClientCreationResponse**](ClientCreationResponse.md)

### Authorization

[clientAccessToken](../README.md#clientAccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Password was successfully reset. |  -  |

