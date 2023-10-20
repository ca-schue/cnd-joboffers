# CompanyApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createNewCompany**](CompanyApi.md#createNewCompany) | **PUT** /companies/register | Creates a new company. |
| [**deleteCompany**](CompanyApi.md#deleteCompany) | **DELETE** /companies/{company-id} |  |
| [**getCompany**](CompanyApi.md#getCompany) | **GET** /companies/{company-id} |  |
| [**getPublicCompanyProfile**](CompanyApi.md#getPublicCompanyProfile) | **GET** /companies/{company-id}/public-profile |  |
| [**inviteUserToCompany**](CompanyApi.md#inviteUserToCompany) | **POST** /companies/{company-id}/invite-user |  |
| [**searchCompanies**](CompanyApi.md#searchCompanies) | **GET** /companies |  |
| [**updateCompanyData**](CompanyApi.md#updateCompanyData) | **POST** /companies/{company-id}/update-company-data |  |


<a name="createNewCompany"></a>
# **createNewCompany**
> Company createNewCompany(companyRegistrationRequest)

Creates a new company.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CompanyApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    CompanyApi apiInstance = new CompanyApi(defaultClient);
    CompanyRegistrationRequest companyRegistrationRequest = new CompanyRegistrationRequest(); // CompanyRegistrationRequest | 
    try {
      Company result = apiInstance.createNewCompany(companyRegistrationRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CompanyApi#createNewCompany");
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
| **companyRegistrationRequest** | [**CompanyRegistrationRequest**](CompanyRegistrationRequest.md)|  | |

### Return type

[**Company**](Company.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Registration was successful. |  -  |

<a name="deleteCompany"></a>
# **deleteCompany**
> deleteCompany(companyId)



Deletes the company.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CompanyApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    CompanyApi apiInstance = new CompanyApi(defaultClient);
    UUID companyId = UUID.randomUUID(); // UUID | 
    try {
      apiInstance.deleteCompany(companyId);
    } catch (ApiException e) {
      System.err.println("Exception when calling CompanyApi#deleteCompany");
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
| **companyId** | **UUID**|  | |

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
| **204** | The company was deleted. |  -  |
| **404** | Company not found for id. |  -  |

<a name="getCompany"></a>
# **getCompany**
> Company getCompany(companyId)



Get company for provided id.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CompanyApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    CompanyApi apiInstance = new CompanyApi(defaultClient);
    UUID companyId = UUID.randomUUID(); // UUID | 
    try {
      Company result = apiInstance.getCompany(companyId);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CompanyApi#getCompany");
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
| **companyId** | **UUID**|  | |

### Return type

[**Company**](Company.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Company was found. |  -  |
| **404** | Company not found for id. |  -  |

<a name="getPublicCompanyProfile"></a>
# **getPublicCompanyProfile**
> PublicCompanyProfile getPublicCompanyProfile(companyId)



Get company for provided id.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CompanyApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    CompanyApi apiInstance = new CompanyApi(defaultClient);
    UUID companyId = UUID.randomUUID(); // UUID | 
    try {
      PublicCompanyProfile result = apiInstance.getPublicCompanyProfile(companyId);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CompanyApi#getPublicCompanyProfile");
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
| **companyId** | **UUID**|  | |

### Return type

[**PublicCompanyProfile**](PublicCompanyProfile.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Company was found. |  -  |
| **404** | Company not found for id. |  -  |

<a name="inviteUserToCompany"></a>
# **inviteUserToCompany**
> inviteUserToCompany(companyId, companyInviteUserRequest)



Invites an user to join the company.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CompanyApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    CompanyApi apiInstance = new CompanyApi(defaultClient);
    UUID companyId = UUID.randomUUID(); // UUID | 
    CompanyInviteUserRequest companyInviteUserRequest = new CompanyInviteUserRequest(); // CompanyInviteUserRequest | 
    try {
      apiInstance.inviteUserToCompany(companyId, companyInviteUserRequest);
    } catch (ApiException e) {
      System.err.println("Exception when calling CompanyApi#inviteUserToCompany");
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
| **companyId** | **UUID**|  | |
| **companyInviteUserRequest** | [**CompanyInviteUserRequest**](CompanyInviteUserRequest.md)|  | |

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
| **201** | Invitation was sent. |  -  |
| **404** | Company not found for id. |  -  |

<a name="searchCompanies"></a>
# **searchCompanies**
> PaginatedPublicCompanyProfileResponse searchCompanies(name, tags, page, size)



Get company for provided id.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CompanyApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    CompanyApi apiInstance = new CompanyApi(defaultClient);
    String name = "name_example"; // String | 
    Set<String> tags = Arrays.asList(); // Set<String> | 
    Integer page = 56; // Integer | 
    Integer size = 56; // Integer | 
    try {
      PaginatedPublicCompanyProfileResponse result = apiInstance.searchCompanies(name, tags, page, size);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CompanyApi#searchCompanies");
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
| **name** | **String**|  | [optional] |
| **tags** | [**Set&lt;String&gt;**](String.md)|  | [optional] |
| **page** | **Integer**|  | [optional] |
| **size** | **Integer**|  | [optional] |

### Return type

[**PaginatedPublicCompanyProfileResponse**](PaginatedPublicCompanyProfileResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Companies for filters were found. |  -  |
| **404** | Company not found for id. |  -  |

<a name="updateCompanyData"></a>
# **updateCompanyData**
> Company updateCompanyData(companyId, updateCompanyDataRequest)



Overrides the current company data with the provided data.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CompanyApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    CompanyApi apiInstance = new CompanyApi(defaultClient);
    UUID companyId = UUID.randomUUID(); // UUID | 
    UpdateCompanyDataRequest updateCompanyDataRequest = new UpdateCompanyDataRequest(); // UpdateCompanyDataRequest | 
    try {
      Company result = apiInstance.updateCompanyData(companyId, updateCompanyDataRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CompanyApi#updateCompanyData");
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
| **companyId** | **UUID**|  | |
| **updateCompanyDataRequest** | [**UpdateCompanyDataRequest**](UpdateCompanyDataRequest.md)|  | |

### Return type

[**Company**](Company.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Registration was successful. |  -  |
| **404** | Company not found for id. |  -  |

