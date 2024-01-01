package thi.cnd.userservice;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.hamcrest.Matchers.matchesRegex;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataMongo
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtProvider jwtProvider;

    private ResultActions testCreateUser(String userProfileEmail, String firstName, String lastName, Jwt accessToken) throws Exception {
        String userCreationRequestJson =
                "{ " +
                        "\"user_profile_email\": \"" + userProfileEmail + "\"," +
                        "\"first_name\": \"" + firstName + "\", " +
                        "\"last_name\": \"" + lastName + "\"" +
                " }";

        // Perform the POST request
        ResultActions creationResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userCreationRequestJson)
                .header("Authorization", "Bearer " + accessToken.getTokenValue()));

        System.out.println(creationResult);

        creationResult.andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(accessToken.getClaimAsString("sub")))
                .andExpect(jsonPath("$.profile.user_profile_email").value(userProfileEmail))
                .andExpect(jsonPath("$.profile.first_name").value(firstName))
                .andExpect(jsonPath("$.profile.last_name").value(lastName))
                .andExpect(jsonPath("$.associations.member_of").isArray())
                .andExpect(jsonPath("$.associations.invited_to").isArray())
                .andExpect(jsonPath("$.associations.owner_of").doesNotExist())
                .andExpect(jsonPath("$.settings.night_mode_active").value(false))
                .andExpect(jsonPath("$.subscription.subscribed").value(false))
                .andExpect(jsonPath("$.subscription.subscribedUntil").doesNotExist());

        return creationResult;
    }

    private ResultActions testGetUser(String userId, Jwt accessToken) throws Exception {

        // Perform the PUT request with Bearer Token Authentication
        ResultActions getResult = mockMvc.perform(MockMvcRequestBuilders.get("/users/{user-id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken.getTokenValue()));

        // Validate the response
        getResult.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId));
        return getResult;
    }

    private ResultActions testUpdateUser(String userId, Jwt accountJwt, String updatedUserProfileEmail, String updatedFirstName, String updatedLastName) throws Exception {
        String userUpdateRequestJson =
                "{ " +
                        "\"user_profile_email\": \"" + updatedUserProfileEmail + "\"," +
                        "\"first_name\": \"" + updatedFirstName + "\", " +
                        "\"last_name\": \"" + updatedLastName + "\"" +
                        " }";

        // Perform the POST request
        ResultActions creationResult = mockMvc.perform(MockMvcRequestBuilders.put("/users/{user-id}/update-user-profile", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userUpdateRequestJson)
                .header("Authorization", "Bearer " + accountJwt.getTokenValue()));

        System.out.println(creationResult);

        creationResult.andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(accountJwt.getClaimAsString("sub")))
                .andExpect(jsonPath("$.profile.user_profile_email").value(updatedUserProfileEmail))
                .andExpect(jsonPath("$.profile.first_name").value(updatedFirstName))
                .andExpect(jsonPath("$.profile.last_name").value(updatedLastName));

        return creationResult;
    }


    private ResultActions testCreateCompany(String ownerId, String companyName, String description, String [] tags, String location, String websiteUrl, String [] socialMedia, Jwt accessToken) throws Exception {
        String companyCreationRequestJson =
                "{ " +
                        "\"owner_id\": \"" + ownerId + "\"," +
                        "\"details\": {" +
                            "\"name\": \"" + companyName + "\", " +
                            "\"description\": \"" + description + "\", " +
                            "\"tags\": " + objectMapper.writeValueAsString(tags) + ", " +
                            "\"location\": \"" + location + "\"" +
                        " }," +
                        "\"links\": {" +
                            "\"website_url\": \"" + websiteUrl + "\", " +
                            "\"social_media\": " + objectMapper.writeValueAsString(socialMedia)  +
                        " }" +
                " }";

        /*
                REQ

               {
                  "owner_id": "string",
                  "details": {
                    "name": "string",
                    "description": "string",
                    "tags": [
                      "string"
                    ],
                    "location": "string"
                  },
                  "links": {
                    "website_url": "string",
                    "social_media": [
                      "string"
                    ]
                  }
                }

         */


        // Perform the POST request
        ResultActions creationResult = mockMvc.perform(MockMvcRequestBuilders.post("/companies/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(companyCreationRequestJson)
                .header("Authorization", "Bearer " + accessToken.getTokenValue()));

        System.out.println(creationResult);

        /*
            RESP
                {
                  "id": "string",
                  "owner": "string",
                  "members": [
                    "string"
                  ],
                  "details": {
                    "name": "string",
                    "description": "string",
                    "tags": [
                      "string"
                    ],
                    "location": "string"
                  },
                  "links": {
                    "website_url": "string",
                    "social_media": [
                      "string"
                    ]
                  },
                  "partner_program": {
                    "partnered": true,
                    "partner_until": "2023-12-15T19:17:00.985Z"
                  }
                }
         */


        creationResult.andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(matchesRegex("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}")))
                .andExpect(jsonPath("$.owner").value(ownerId))
                .andExpect(jsonPath("$.members").isArray())
                .andExpect(jsonPath("$.details.name").value(companyName))
                .andExpect(jsonPath("$.details.description").value(description))
                .andExpect(jsonPath("$.details.tags").value(Matchers.contains(tags)))
                .andExpect(jsonPath("$.details.location").value(location))
                .andExpect(jsonPath("$.links.website_url").value(websiteUrl))
                .andExpect(jsonPath("$.links.social_media").value(Matchers.contains(socialMedia)))
                .andExpect(jsonPath("$.partner_program.partnered").value(false))
                .andExpect(jsonPath("$.partner_program.partner_until").doesNotExist());

        return creationResult;
    }

    private void testInviteUser(String companyId, String invitedUserEmail, Jwt memberAccessToken) throws Exception {
        String inviteUserRequestJson =
                "{ " +
                        "\"user_profile_email\": \"" + invitedUserEmail + "\"" +
                " }";

        // Perform the POST request
        ResultActions invitationResult = mockMvc.perform(MockMvcRequestBuilders.post("/companies/{company-id}/invite-user", companyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inviteUserRequestJson)
                .header("Authorization", "Bearer " + memberAccessToken.getTokenValue()));

        System.out.println(invitationResult);

        invitationResult.andExpect(status().is(200));
    }

    private void testAcceptInvitation(String invitedUserId, String invitedToCompanyId, Jwt invitedUserAccessToken) throws Exception {
        String invitationAcceptRequestJson =
                "{ " +
                        "\"company_id\": \"" + invitedToCompanyId + "\"" +
                " }";

        // Perform the POST request
        ResultActions invitationAcceptResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/{user-id}/accept-company-invitation", invitedUserId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invitationAcceptRequestJson)
                .header("Authorization", "Bearer " + invitedUserAccessToken.getTokenValue()));

        System.out.println(invitationAcceptResult);

        invitationAcceptResult.andExpect(status().is(200));
    }

    private void testDeleteUser(String deletedUserId, Jwt deletedUserJwt) throws Exception {
        // Perform the POST request
        ResultActions invitationAcceptResult = mockMvc.perform(MockMvcRequestBuilders.delete("/users/{user-id}", deletedUserId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + deletedUserJwt.getTokenValue()));

        System.out.println(invitationAcceptResult);

        invitationAcceptResult.andExpect(status().is(204));
    }

    private ResultActions testGetCompanyId(String companyId, Jwt ownerMemberInvitedOrClientJwt) throws Exception {
        // Perform the POST request
        ResultActions getCompanyResult = mockMvc.perform(MockMvcRequestBuilders.get("/companies/{company-id}", companyId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + ownerMemberInvitedOrClientJwt.getTokenValue()));

        getCompanyResult.andExpect(status().is(200));
        return getCompanyResult;
    }


    @Test
    public void clientCompanyLifecycle() throws Exception {

        System.out.println("Test create user");
        String accountId = UUID.randomUUID().toString();
        String userProfileEmail = "user@mail";
        String firstName = "userFirstName";
        String lastName = "userLastName";
        Jwt accountJwt = jwtProvider.mintAccountJwt(accountId, true);
        testCreateUser(userProfileEmail, firstName, lastName, accountJwt);


        System.out.println("Test create 2nd user with same mail");
        assertEquals(
                "Response status expected:<200> but was:<409>",
                assertThrows(AssertionError.class, () -> testCreateUser(userProfileEmail, "2ndFirstName", "2ndLastName", jwtProvider.mintAccountJwt(UUID.randomUUID().toString(), true))).getMessage()
        );


        System.out.println("Test get user as client");
        Jwt clientJwtValidScope = jwtProvider.mintClientJwt("testclient", new String[]{"user-service"}, new String[]{"getUser", "getCompany"});
        ResultActions getResult = testGetUser(accountId, clientJwtValidScope);


        System.out.println("Test get user as invalid client");
        Jwt clientJwtInValidScope = jwtProvider.mintClientJwt("testclient", new String[]{"user-service"}, new String[]{"cannotGetUser", "cannotGetCompany"});
        assertEquals(
                "Status expected:<200> but was:<403>",
                assertThrows(AssertionError.class, () -> testGetUser(accountId, clientJwtInValidScope)).getMessage()
        );


        System.out.println("Test update user");
        String updatedUserProfileEmail = "new@mail";
        String updatedFirstName = "newFirstName";
        String updatedLastName = "newLastName";
        testUpdateUser(accountId, accountJwt, updatedUserProfileEmail, updatedFirstName, updatedLastName);


        System.out.println("Test create company");
        String companyName = "Test Company";
        String description = "Test Description";
        String [] tags = {"Tag1", "Tag2"};
        String location = "Test Location";
        String websiteUrl = "www.test-url.com";
        String [] socialMedia = {"x.com/test-comp", "linkedin/test-comp"};
        ResultActions createCompanyResult = testCreateCompany(accountId, companyName, description, tags, location, websiteUrl, socialMedia, accountJwt);
        String companyId = JsonPath.read(createCompanyResult.andReturn().getResponse().getContentAsString(), "$.id");


        System.out.println("Test owner invites user");
        String invitedAccountId = UUID.randomUUID().toString();
        String invitedUserProfileEmail = "user@invited";
        String invitedFirstName = "invitedUserFirstName";
        String invitedLastName = "invitedUserLastName";
        Jwt invitedAccountJwt = jwtProvider.mintAccountJwt(invitedAccountId, true);
        testCreateUser(invitedUserProfileEmail, invitedFirstName, invitedLastName, invitedAccountJwt);
        testInviteUser(companyId, invitedUserProfileEmail, accountJwt);
        ResultActions getInvitedUserResult = testGetUser(invitedAccountId, invitedAccountJwt);
        getInvitedUserResult
                .andExpect(jsonPath("$.associations.invited_to").value(Matchers.contains(companyId)))
                .andExpect(jsonPath("$.associations.member_of").isEmpty()
                );

        System.out.println("Test accept invitation");
        testAcceptInvitation(invitedAccountId, companyId, invitedAccountJwt);
        ResultActions getInviteAcceptedUserResult = testGetUser(invitedAccountId, invitedAccountJwt);
        getInviteAcceptedUserResult
                .andExpect(jsonPath("$.associations.invited_to").isEmpty())
                .andExpect(jsonPath("$.associations.member_of").value(Matchers.contains(companyId))
                );


        System.out.println("Test Member invites user");
        String invitedAccountId2 = UUID.randomUUID().toString();
        String invitedUserProfileEmail2 = "user@invited2";
        String invitedFirstName2 = "invitedUserFirstName2";
        String invitedLastName2 = "invitedUserLastName2";
        Jwt invitedAccountJwt2 = jwtProvider.mintAccountJwt(invitedAccountId2, true);
        testCreateUser(invitedUserProfileEmail2, invitedFirstName2, invitedLastName2, invitedAccountJwt2);
        testInviteUser(companyId, invitedUserProfileEmail2, invitedAccountJwt);
        ResultActions getInvitedUserResult2 = testGetUser(invitedAccountId2, invitedAccountJwt2);
        getInvitedUserResult2
                .andExpect(jsonPath("$.associations.invited_to").value(Matchers.contains(companyId)))
                .andExpect(jsonPath("$.associations.member_of").isEmpty()
                );

        System.out.println("Test delete user (deletes company)");
        testDeleteUser(accountId, accountJwt);
        assertEquals(
                "Response status expected:<200> but was:<404>",
                assertThrows(AssertionError.class, () -> testGetCompanyId(companyId, clientJwtValidScope)).getMessage()
        );
        testGetUser(invitedAccountId, invitedAccountJwt)
                .andExpect(jsonPath("$.associations.invited_to").isEmpty())
                .andExpect(jsonPath("$.associations.member_of").isEmpty());
        testGetUser(invitedAccountId2, invitedAccountJwt2)
                .andExpect(jsonPath("$.associations.invited_to").isEmpty())
                .andExpect(jsonPath("$.associations.member_of").isEmpty());
    }
}
