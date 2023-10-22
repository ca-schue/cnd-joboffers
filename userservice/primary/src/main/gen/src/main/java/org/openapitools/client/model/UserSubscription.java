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
import java.time.OffsetDateTime;

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
 * UserSubscription
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-10-20T23:00:55.910112700+02:00[Europe/Berlin]")
public class UserSubscription {
  public static final String SERIALIZED_NAME_SUBSCRIBED = "subscribed";
  @SerializedName(SERIALIZED_NAME_SUBSCRIBED)
  private Boolean subscribed;

  public static final String SERIALIZED_NAME_SUBSCRIBED_UNTIL = "subscribedUntil";
  @SerializedName(SERIALIZED_NAME_SUBSCRIBED_UNTIL)
  private OffsetDateTime subscribedUntil;

  public UserSubscription() {
  }

  public UserSubscription subscribed(Boolean subscribed) {
    
    this.subscribed = subscribed;
    return this;
  }

   /**
   * Get subscribed
   * @return subscribed
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")

  public Boolean getSubscribed() {
    return subscribed;
  }


  public void setSubscribed(Boolean subscribed) {
    this.subscribed = subscribed;
  }


  public UserSubscription subscribedUntil(OffsetDateTime subscribedUntil) {
    
    this.subscribedUntil = subscribedUntil;
    return this;
  }

   /**
   * Get subscribedUntil
   * @return subscribedUntil
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public OffsetDateTime getSubscribedUntil() {
    return subscribedUntil;
  }


  public void setSubscribedUntil(OffsetDateTime subscribedUntil) {
    this.subscribedUntil = subscribedUntil;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserSubscription userSubscription = (UserSubscription) o;
    return Objects.equals(this.subscribed, userSubscription.subscribed) &&
        Objects.equals(this.subscribedUntil, userSubscription.subscribedUntil);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subscribed, subscribedUntil);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserSubscription {\n");
    sb.append("    subscribed: ").append(toIndentedString(subscribed)).append("\n");
    sb.append("    subscribedUntil: ").append(toIndentedString(subscribedUntil)).append("\n");
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
    openapiFields.add("subscribed");
    openapiFields.add("subscribedUntil");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
    openapiRequiredFields.add("subscribed");
  }

 /**
  * Validates the JSON Object and throws an exception if issues found
  *
  * @param jsonObj JSON Object
  * @throws IOException if the JSON Object is invalid with respect to UserSubscription
  */
  public static void validateJsonObject(JsonObject jsonObj) throws IOException {
      if (jsonObj == null) {
        if (UserSubscription.openapiRequiredFields.isEmpty()) {
          return;
        } else { // has required fields
          throw new IllegalArgumentException(String.format("The required field(s) %s in UserSubscription is not found in the empty JSON string", UserSubscription.openapiRequiredFields.toString()));
        }
      }

      Set<Entry<String, JsonElement>> entries = jsonObj.entrySet();
      // check to see if the JSON string contains additional fields
      for (Entry<String, JsonElement> entry : entries) {
        if (!UserSubscription.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `UserSubscription` properties. JSON: %s", entry.getKey(), jsonObj.toString()));
        }
      }

      // check to make sure all required properties/fields are present in the JSON string
      for (String requiredField : UserSubscription.openapiRequiredFields) {
        if (jsonObj.get(requiredField) == null) {
          throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonObj.toString()));
        }
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!UserSubscription.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'UserSubscription' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<UserSubscription> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(UserSubscription.class));

       return (TypeAdapter<T>) new TypeAdapter<UserSubscription>() {
           @Override
           public void write(JsonWriter out, UserSubscription value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public UserSubscription read(JsonReader in) throws IOException {
             JsonObject jsonObj = elementAdapter.read(in).getAsJsonObject();
             validateJsonObject(jsonObj);
             return thisAdapter.fromJsonTree(jsonObj);
           }

       }.nullSafe();
    }
  }

 /**
  * Create an instance of UserSubscription given an JSON string
  *
  * @param jsonString JSON string
  * @return An instance of UserSubscription
  * @throws IOException if the JSON string is invalid with respect to UserSubscription
  */
  public static UserSubscription fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, UserSubscription.class);
  }

 /**
  * Convert an instance of UserSubscription to an JSON string
  *
  * @return JSON string
  */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}
