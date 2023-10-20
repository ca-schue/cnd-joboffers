# AuthApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getRequestUserData**](AuthApi.md#getRequestUserData) | **GET** /token-introspection | Get the user data from the provided access token. |


<a name="getRequestUserData"></a>
# **getRequestUserData**
> User getRequestUserData()

Get the user data from the provided access token.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.AuthApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    AuthApi apiInstance = new AuthApi(defaultClient);
    try {
      User result = apiInstance.getRequestUserData();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling AuthApi#getRequestUserData");
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

[**User**](User.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Token is valid and user data could be retrieved. |  -  |

