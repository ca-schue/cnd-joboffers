# AuthEndpointsApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**tokenIntrospection**](AuthEndpointsApi.md#tokenIntrospection) | **GET** /token-introspection |  |


<a name="tokenIntrospection"></a>
# **tokenIntrospection**
> AuthenticatedSubjectResponse tokenIntrospection()



Get detailed account or client data from the provided access token.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.AuthEndpointsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");
    
    // Configure HTTP bearer authorization: accountAccessToken
    HttpBearerAuth accountAccessToken = (HttpBearerAuth) defaultClient.getAuthentication("accountAccessToken");
    accountAccessToken.setBearerToken("BEARER TOKEN");

    // Configure HTTP bearer authorization: clientAccessToken
    HttpBearerAuth clientAccessToken = (HttpBearerAuth) defaultClient.getAuthentication("clientAccessToken");
    clientAccessToken.setBearerToken("BEARER TOKEN");

    AuthEndpointsApi apiInstance = new AuthEndpointsApi(defaultClient);
    try {
      AuthenticatedSubjectResponse result = apiInstance.tokenIntrospection();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling AuthEndpointsApi#tokenIntrospection");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**AuthenticatedSubjectResponse**](AuthenticatedSubjectResponse.md)

### Authorization

[accountAccessToken](../README.md#accountAccessToken), [clientAccessToken](../README.md#clientAccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Token is valid and user data could be retrieved. |  -  |

