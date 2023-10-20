# AccountManagementApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**deleteAccount**](AccountManagementApi.md#deleteAccount) | **DELETE** /accounts/{accountId} |  |
| [**registerInternalAccount**](AccountManagementApi.md#registerInternalAccount) | **PUT** /accounts/registerInternalAccount |  |


<a name="deleteAccount"></a>
# **deleteAccount**
> deleteAccount(accountId)



Deletes the account. {accountId} must match account id in access token

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.AccountManagementApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");
    
    // Configure HTTP bearer authorization: accountAccessToken
    HttpBearerAuth accountAccessToken = (HttpBearerAuth) defaultClient.getAuthentication("accountAccessToken");
    accountAccessToken.setBearerToken("BEARER TOKEN");

    AccountManagementApi apiInstance = new AccountManagementApi(defaultClient);
    UUID accountId = UUID.randomUUID(); // UUID | 
    try {
      apiInstance.deleteAccount(accountId);
    } catch (ApiException e) {
      System.err.println("Exception when calling AccountManagementApi#deleteAccount");
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
| **accountId** | **UUID**|  | |

### Return type

null (empty response body)

### Authorization

[accountAccessToken](../README.md#accountAccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **204** | User was deleted. |  -  |
| **404** | User not found for id. |  -  |

<a name="registerInternalAccount"></a>
# **registerInternalAccount**
> Account registerInternalAccount(internalAccountRegistrationRequest)



Registers a new internal account with the provided credentials.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.AccountManagementApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    AccountManagementApi apiInstance = new AccountManagementApi(defaultClient);
    InternalAccountRegistrationRequest internalAccountRegistrationRequest = new InternalAccountRegistrationRequest(); // InternalAccountRegistrationRequest | 
    try {
      Account result = apiInstance.registerInternalAccount(internalAccountRegistrationRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling AccountManagementApi#registerInternalAccount");
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
| **internalAccountRegistrationRequest** | [**InternalAccountRegistrationRequest**](InternalAccountRegistrationRequest.md)|  | |

### Return type

[**Account**](Account.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Interal account registration was successful. |  -  |
| **409** | Account with the email already exists. |  -  |
| **0** | Returned if any error occurred during the request. |  -  |

