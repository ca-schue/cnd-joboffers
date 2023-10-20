# UserApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**acceptInvitation**](UserApi.md#acceptInvitation) | **POST** /users/{user-id}/accept-company-invitation | Accepts the invitation to be a member of a company. |
| [**deleteUser**](UserApi.md#deleteUser) | **DELETE** /users/{user-id} |  |
| [**getPublicUserProfile**](UserApi.md#getPublicUserProfile) | **GET** /users/{user-id}/public-profile |  |
| [**getUser**](UserApi.md#getUser) | **GET** /users/{user-id} |  |
| [**registerNewUser**](UserApi.md#registerNewUser) | **PUT** /users/register | Register a new user. |
| [**subscribe**](UserApi.md#subscribe) | **POST** /users/{user-id}/subscribe | Makes the user a subscriber or extends their subscription. |
| [**updateUserData**](UserApi.md#updateUserData) | **POST** /users/{user-id}/update-user-data |  |
| [**updateUserProfileEmail**](UserApi.md#updateUserProfileEmail) | **POST** /users/{user-id}/update-user-profile-email |  |


<a name="acceptInvitation"></a>
# **acceptInvitation**
> acceptInvitation(userId, acceptCompanyInvitationRequest)

Accepts the invitation to be a member of a company.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UserApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    UserApi apiInstance = new UserApi(defaultClient);
    UUID userId = UUID.randomUUID(); // UUID | 
    AcceptCompanyInvitationRequest acceptCompanyInvitationRequest = new AcceptCompanyInvitationRequest(); // AcceptCompanyInvitationRequest | 
    try {
      apiInstance.acceptInvitation(userId, acceptCompanyInvitationRequest);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserApi#acceptInvitation");
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
| **userId** | **UUID**|  | |
| **acceptCompanyInvitationRequest** | [**AcceptCompanyInvitationRequest**](AcceptCompanyInvitationRequest.md)|  | |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **204** | Invitation was accepted. |  -  |

<a name="deleteUser"></a>
# **deleteUser**
> deleteUser(userId)



Deletes the user.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UserApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    UserApi apiInstance = new UserApi(defaultClient);
    UUID userId = UUID.randomUUID(); // UUID | 
    try {
      apiInstance.deleteUser(userId);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserApi#deleteUser");
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
| **userId** | **UUID**|  | |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **204** | User was deleted. |  -  |
| **404** | User not found for id. |  -  |

<a name="getPublicUserProfile"></a>
# **getPublicUserProfile**
> PublicUserProfile getPublicUserProfile(userId)



Get user public profile of provided user id.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UserApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    UserApi apiInstance = new UserApi(defaultClient);
    UUID userId = UUID.randomUUID(); // UUID | 
    try {
      PublicUserProfile result = apiInstance.getPublicUserProfile(userId);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserApi#getPublicUserProfile");
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
| **userId** | **UUID**|  | |

### Return type

[**PublicUserProfile**](PublicUserProfile.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | User was found. |  -  |
| **404** | User not found for id. |  -  |

<a name="getUser"></a>
# **getUser**
> User getUser(userId)



Get user of provided user id.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UserApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    UserApi apiInstance = new UserApi(defaultClient);
    UUID userId = UUID.randomUUID(); // UUID | 
    try {
      User result = apiInstance.getUser(userId);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserApi#getUser");
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
| **userId** | **UUID**|  | |

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
| **200** | User was found. |  -  |
| **404** | User not found for id. |  -  |

<a name="registerNewUser"></a>
# **registerNewUser**
> User registerNewUser(userRegistrationRequest)

Register a new user.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UserApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    UserApi apiInstance = new UserApi(defaultClient);
    UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest(); // UserRegistrationRequest | 
    try {
      User result = apiInstance.registerNewUser(userRegistrationRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserApi#registerNewUser");
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
| **userRegistrationRequest** | [**UserRegistrationRequest**](UserRegistrationRequest.md)|  | |

### Return type

[**User**](User.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Registration was successful. |  -  |

<a name="subscribe"></a>
# **subscribe**
> UserSubscription subscribe(userId, extendUserSubscriptionRequest)

Makes the user a subscriber or extends their subscription.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UserApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    UserApi apiInstance = new UserApi(defaultClient);
    UUID userId = UUID.randomUUID(); // UUID | 
    ExtendUserSubscriptionRequest extendUserSubscriptionRequest = new ExtendUserSubscriptionRequest(); // ExtendUserSubscriptionRequest | 
    try {
      UserSubscription result = apiInstance.subscribe(userId, extendUserSubscriptionRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserApi#subscribe");
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
| **userId** | **UUID**|  | |
| **extendUserSubscriptionRequest** | [**ExtendUserSubscriptionRequest**](ExtendUserSubscriptionRequest.md)|  | |

### Return type

[**UserSubscription**](UserSubscription.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Subscription was successful. |  -  |

<a name="updateUserData"></a>
# **updateUserData**
> User updateUserData(userId, updateUserDataRequest)



Update the user data associated with this user.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UserApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    UserApi apiInstance = new UserApi(defaultClient);
    UUID userId = UUID.randomUUID(); // UUID | 
    UpdateUserDataRequest updateUserDataRequest = new UpdateUserDataRequest(); // UpdateUserDataRequest | 
    try {
      User result = apiInstance.updateUserData(userId, updateUserDataRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserApi#updateUserData");
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
| **userId** | **UUID**|  | |
| **updateUserDataRequest** | [**UpdateUserDataRequest**](UpdateUserDataRequest.md)|  | |

### Return type

[**User**](User.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | User data was updated. |  -  |
| **404** | User not found for id. |  -  |

<a name="updateUserProfileEmail"></a>
# **updateUserProfileEmail**
> User updateUserProfileEmail(userId, updateUserProfileEmailRequest)



Update the user profile email associated to this user.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UserApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    UserApi apiInstance = new UserApi(defaultClient);
    UUID userId = UUID.randomUUID(); // UUID | 
    UpdateUserProfileEmailRequest updateUserProfileEmailRequest = new UpdateUserProfileEmailRequest(); // UpdateUserProfileEmailRequest | 
    try {
      User result = apiInstance.updateUserProfileEmail(userId, updateUserProfileEmailRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserApi#updateUserProfileEmail");
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
| **userId** | **UUID**|  | |
| **updateUserProfileEmailRequest** | [**UpdateUserProfileEmailRequest**](UpdateUserProfileEmailRequest.md)|  | |

### Return type

[**User**](User.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | User data was updated. |  -  |
| **404** | User not found for id. |  -  |

