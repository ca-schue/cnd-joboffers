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
import java.util.UUID;

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
 * UserCompanyAssociation
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-10-20T23:00:55.910112700+02:00[Europe/Berlin]")
public class UserCompanyAssociation {
  public static final String SERIALIZED_NAME_MEMBER_OF = "member_of";
  @SerializedName(SERIALIZED_NAME_MEMBER_OF)
  private List<UUID> memberOf = new ArrayList<>();

  public static final String SERIALIZED_NAME_INVITED_TO = "invited_to";
  @SerializedName(SERIALIZED_NAME_INVITED_TO)
  private List<UUID> invitedTo = new ArrayList<>();

  public UserCompanyAssociation() {
  }

  public UserCompanyAssociation memberOf(List<UUID> memberOf) {
    
    this.memberOf = memberOf;
    return this;
  }

  public UserCompanyAssociation addMemberOfItem(UUID memberOfItem) {
    this.memberOf.add(memberOfItem);
    return this;
  }

   /**
   * Get memberOf
   * @return memberOf
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")

  public List<UUID> getMemberOf() {
    return memberOf;
  }


  public void setMemberOf(List<UUID> memberOf) {
    this.memberOf = memberOf;
  }


  public UserCompanyAssociation invitedTo(List<UUID> invitedTo) {
    
    this.invitedTo = invitedTo;
    return this;
  }

  public UserCompanyAssociation addInvitedToItem(UUID invitedToItem) {
    this.invitedTo.add(invitedToItem);
    return this;
  }

   /**
   * Get invitedTo
   * @return invitedTo
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")

  public List<UUID> getInvitedTo() {
    return invitedTo;
  }


  public void setInvitedTo(List<UUID> invitedTo) {
    this.invitedTo = invitedTo;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserCompanyAssociation userCompanyAssociation = (UserCompanyAssociation) o;
    return Objects.equals(this.memberOf, userCompanyAssociation.memberOf) &&
        Objects.equals(this.invitedTo, userCompanyAssociation.invitedTo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(memberOf, invitedTo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserCompanyAssociation {\n");
    sb.append("    memberOf: ").append(toIndentedString(memberOf)).append("\n");
    sb.append("    invitedTo: ").append(toIndentedString(invitedTo)).append("\n");
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
    openapiFields.add("member_of");
    openapiFields.add("invited_to");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
    openapiRequiredFields.add("member_of");
    openapiRequiredFields.add("invited_to");
  }

 /**
  * Validates the JSON Object and throws an exception if issues found
  *
  * @param jsonObj JSON Object
  * @throws IOException if the JSON Object is invalid with respect to UserCompanyAssociation
  */
  public static void validateJsonObject(JsonObject jsonObj) throws IOException {
      if (jsonObj == null) {
        if (UserCompanyAssociation.openapiRequiredFields.isEmpty()) {
          return;
        } else { // has required fields
          throw new IllegalArgumentException(String.format("The required field(s) %s in UserCompanyAssociation is not found in the empty JSON string", UserCompanyAssociation.openapiRequiredFields.toString()));
        }
      }

      Set<Entry<String, JsonElement>> entries = jsonObj.entrySet();
      // check to see if the JSON string contains additional fields
      for (Entry<String, JsonElement> entry : entries) {
        if (!UserCompanyAssociation.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `UserCompanyAssociation` properties. JSON: %s", entry.getKey(), jsonObj.toString()));
        }
      }

      // check to make sure all required properties/fields are present in the JSON string
      for (String requiredField : UserCompanyAssociation.openapiRequiredFields) {
        if (jsonObj.get(requiredField) == null) {
          throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonObj.toString()));
        }
      }
      // ensure the json data is an array
      if ((jsonObj.get("member_of") != null && !jsonObj.get("member_of").isJsonNull()) && !jsonObj.get("member_of").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `member_of` to be an array in the JSON string but got `%s`", jsonObj.get("member_of").toString()));
      }
      // ensure the json data is an array
      if ((jsonObj.get("invited_to") != null && !jsonObj.get("invited_to").isJsonNull()) && !jsonObj.get("invited_to").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `invited_to` to be an array in the JSON string but got `%s`", jsonObj.get("invited_to").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!UserCompanyAssociation.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'UserCompanyAssociation' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<UserCompanyAssociation> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(UserCompanyAssociation.class));

       return (TypeAdapter<T>) new TypeAdapter<UserCompanyAssociation>() {
           @Override
           public void write(JsonWriter out, UserCompanyAssociation value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public UserCompanyAssociation read(JsonReader in) throws IOException {
             JsonObject jsonObj = elementAdapter.read(in).getAsJsonObject();
             validateJsonObject(jsonObj);
             return thisAdapter.fromJsonTree(jsonObj);
           }

       }.nullSafe();
    }
  }

 /**
  * Create an instance of UserCompanyAssociation given an JSON string
  *
  * @param jsonString JSON string
  * @return An instance of UserCompanyAssociation
  * @throws IOException if the JSON string is invalid with respect to UserCompanyAssociation
  */
  public static UserCompanyAssociation fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, UserCompanyAssociation.class);
  }

 /**
  * Convert an instance of UserCompanyAssociation to an JSON string
  *
  * @return JSON string
  */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

