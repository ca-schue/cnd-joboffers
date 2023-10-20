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


package org.openapitools.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.openapitools.client.model.Pagination;
import org.openapitools.client.model.PublicCompanyProfile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.openapitools.client.JSON;

/**
 * PaginatedPublicCompanyProfileResponse
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-10-20T23:00:55.910112700+02:00[Europe/Berlin]")
public class PaginatedPublicCompanyProfileResponse {
  public static final String SERIALIZED_NAME_PAGINATION = "pagination";
  @SerializedName(SERIALIZED_NAME_PAGINATION)
  private Pagination pagination;

  public static final String SERIALIZED_NAME_CONTENT = "content";
  @SerializedName(SERIALIZED_NAME_CONTENT)
  private List<PublicCompanyProfile> content = null;

  public PaginatedPublicCompanyProfileResponse() {
  }

  public PaginatedPublicCompanyProfileResponse pagination(Pagination pagination) {
    
    this.pagination = pagination;
    return this;
  }

   /**
   * Get pagination
   * @return pagination
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Pagination getPagination() {
    return pagination;
  }


  public void setPagination(Pagination pagination) {
    this.pagination = pagination;
  }


  public PaginatedPublicCompanyProfileResponse content(List<PublicCompanyProfile> content) {
    
    this.content = content;
    return this;
  }

  public PaginatedPublicCompanyProfileResponse addContentItem(PublicCompanyProfile contentItem) {
    if (this.content == null) {
      this.content = new ArrayList<>();
    }
    this.content.add(contentItem);
    return this;
  }

   /**
   * Get content
   * @return content
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public List<PublicCompanyProfile> getContent() {
    return content;
  }


  public void setContent(List<PublicCompanyProfile> content) {
    this.content = content;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PaginatedPublicCompanyProfileResponse paginatedPublicCompanyProfileResponse = (PaginatedPublicCompanyProfileResponse) o;
    return Objects.equals(this.pagination, paginatedPublicCompanyProfileResponse.pagination) &&
        Objects.equals(this.content, paginatedPublicCompanyProfileResponse.content);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pagination, content);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaginatedPublicCompanyProfileResponse {\n");
    sb.append("    pagination: ").append(toIndentedString(pagination)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }


  public static HashSet<String> openapiFields;
  public static HashSet<String> openapiRequiredFields;

  static {
    // a set of all properties/fields (JSON key names)
    openapiFields = new HashSet<String>();
    openapiFields.add("pagination");
    openapiFields.add("content");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

 /**
  * Validates the JSON Object and throws an exception if issues found
  *
  * @param jsonObj JSON Object
  * @throws IOException if the JSON Object is invalid with respect to PaginatedPublicCompanyProfileResponse
  */
  public static void validateJsonObject(JsonObject jsonObj) throws IOException {
      if (jsonObj == null) {
        if (PaginatedPublicCompanyProfileResponse.openapiRequiredFields.isEmpty()) {
          return;
        } else { // has required fields
          throw new IllegalArgumentException(String.format("The required field(s) %s in PaginatedPublicCompanyProfileResponse is not found in the empty JSON string", PaginatedPublicCompanyProfileResponse.openapiRequiredFields.toString()));
        }
      }

      Set<Entry<String, JsonElement>> entries = jsonObj.entrySet();
      // check to see if the JSON string contains additional fields
      for (Entry<String, JsonElement> entry : entries) {
        if (!PaginatedPublicCompanyProfileResponse.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `PaginatedPublicCompanyProfileResponse` properties. JSON: %s", entry.getKey(), jsonObj.toString()));
        }
      }
      // validate the optional field `pagination`
      if (jsonObj.get("pagination") != null && !jsonObj.get("pagination").isJsonNull()) {
        Pagination.validateJsonObject(jsonObj.getAsJsonObject("pagination"));
      }
      if (jsonObj.get("content") != null && !jsonObj.get("content").isJsonNull()) {
        JsonArray jsonArraycontent = jsonObj.getAsJsonArray("content");
        if (jsonArraycontent != null) {
          // ensure the json data is an array
          if (!jsonObj.get("content").isJsonArray()) {
            throw new IllegalArgumentException(String.format("Expected the field `content` to be an array in the JSON string but got `%s`", jsonObj.get("content").toString()));
          }

          // validate the optional field `content` (array)
          for (int i = 0; i < jsonArraycontent.size(); i++) {
            PublicCompanyProfile.validateJsonObject(jsonArraycontent.get(i).getAsJsonObject());
          };
        }
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!PaginatedPublicCompanyProfileResponse.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'PaginatedPublicCompanyProfileResponse' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<PaginatedPublicCompanyProfileResponse> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(PaginatedPublicCompanyProfileResponse.class));

       return (TypeAdapter<T>) new TypeAdapter<PaginatedPublicCompanyProfileResponse>() {
           @Override
           public void write(JsonWriter out, PaginatedPublicCompanyProfileResponse value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public PaginatedPublicCompanyProfileResponse read(JsonReader in) throws IOException {
             JsonObject jsonObj = elementAdapter.read(in).getAsJsonObject();
             validateJsonObject(jsonObj);
             return thisAdapter.fromJsonTree(jsonObj);
           }

       }.nullSafe();
    }
  }

 /**
  * Create an instance of PaginatedPublicCompanyProfileResponse given an JSON string
  *
  * @param jsonString JSON string
  * @return An instance of PaginatedPublicCompanyProfileResponse
  * @throws IOException if the JSON string is invalid with respect to PaginatedPublicCompanyProfileResponse
  */
  public static PaginatedPublicCompanyProfileResponse fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, PaginatedPublicCompanyProfileResponse.class);
  }

 /**
  * Convert an instance of PaginatedPublicCompanyProfileResponse to an JSON string
  *
  * @return JSON string
  */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

