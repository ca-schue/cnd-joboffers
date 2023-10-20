# AccountLoginApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**loginInternalAccount**](AccountLoginApi.md#loginInternalAccount) | **POST** /accounts/loginInternalAccount |  |
| [**loginOIDCAccount**](AccountLoginApi.md#loginOIDCAccount) | **POST** /accounts/loginOIDCAccount |  |


<a name="loginInternalAccount"></a>
# **loginInternalAccount**
> AccessTokenResponse loginInternalAccount()



Login to an internal account with basic auth. Returns an access token with claim \&quot;subject-type &#x3D; account\&quot;

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.AccountLoginApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");
    
    // Configure HTTP basic authorization: internalAccountBasicAuth
    HttpBasicAuth internalAccountBasicAuth = (HttpBasicAuth) defaultClient.getAuthentication("internalAccountBasicAuth");
    internalAccountBasicAuth.setUsername("YOUR USERNAME");
    internalAccountBasicAuth.setPassword("YOUR PASSWORD");

    AccountLoginApi apiInstance = new AccountLoginApi(defaultClient);
    try {
      AccessTokenResponse result = apiInstance.loginInternalAccount();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling AccountLoginApi#loginInternalAccount");
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

[**AccessTokenResponse**](AccessTokenResponse.md)

### Authorization

[internalAccountBasicAuth](../README.md#internalAccountBasicAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Login to interal Account was successful |  -  |
| **401** | Wrong credentials |  -  |

<a name="loginOIDCAccount"></a>
# **loginOIDCAccount**
> AccessTokenResponse loginOIDCAccount()



Login with via ID-token provided by OIDC provider (issuer must be supported by auth. server). Creates new account if email does not exist. Returns access token with access token with claim \&quot;subject-type &#x3D; account\&quot;.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.AccountLoginApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");
    
    // Configure HTTP bearer authorization: oidcIdToken
    HttpBearerAuth oidcIdToken = (HttpBearerAuth) defaultClient.getAuthentication("oidcIdToken");
    oidcIdToken.setBearerToken("BEARER TOKEN");

    AccountLoginApi apiInstance = new AccountLoginApi(defaultClient);
    try {
      AccessTokenResponse result = apiInstance.loginOIDCAccount();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling AccountLoginApi#loginOIDCAccount");
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

[**AccessTokenResponse**](AccessTokenResponse.md)

### Authorization

[oidcIdToken](../README.md#oidcIdToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Login with OIDC was successful. Account exists. |  -  |
| **201** | Login with OIDC was successful. New Account created. |  -  |
| **401** | Unsupported OIDC identity provider (ID-Token issuer). |  -  |

