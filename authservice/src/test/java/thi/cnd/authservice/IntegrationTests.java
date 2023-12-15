package thi.cnd.authservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.matchesRegex;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataMongo
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JWKSource<SecurityContext> jwkSource;

    @Autowired
    private JwtDecoder jwtDecoder;

    private ResultActions testInternalAccountRegistration(String email, String password) throws Exception {
        // Prepare the request payload
        String registrationRequestJson = "{ \"email\": \"" + email + "\", \"password\": \"" + password + "\" }";

        // Perform the POST request
        ResultActions registrationResult = mockMvc.perform(MockMvcRequestBuilders.post("/accounts/registerInternalAccount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registrationRequestJson));

        // Validate the response
        registrationResult.andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.account.accountType").value("InternalAccount"))
                .andExpect(jsonPath("$.account.id").value(matchesRegex("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}")))
                .andExpect(jsonPath("$.account.email").value(email))
                .andExpect(jsonPath("$.access_token").isNotEmpty());
        return registrationResult;
    }

    private ResultActions testInternalAccountLogin(String email, String password) throws Exception {
        // Perform the POST request with Basic Authentication
        ResultActions loginResult = mockMvc.perform(MockMvcRequestBuilders.post("/accounts/loginInternalAccount")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString((email + ":" + password).getBytes())));

        // Validate the response
        loginResult.andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.account.accountType").value("InternalAccount"))
                .andExpect(jsonPath("$.account.id").value(matchesRegex("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}")))
                .andExpect(jsonPath("$.account.email").value(email))
                .andExpect(jsonPath("$.access_token").isNotEmpty());
        return loginResult;
    }

    private ResultActions testInternalAccountEmailUpdate(String updatedEmail, String accountId, String accessToken) throws Exception {
        // Prepare the request payload
        String emailUpdateRequestJson = "{ \"new_email\": \"" + updatedEmail + "\" }";

        // Perform the PUT request with Bearer Token Authentication
        ResultActions updateResult = mockMvc.perform(MockMvcRequestBuilders.put("/accounts/{accountId}/update-internal-email", accountId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .content(emailUpdateRequestJson));

        // Validate the response
        updateResult.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountType").value("InternalAccount"))
                .andExpect(jsonPath("$.id").value(matchesRegex("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}")))
                .andExpect(jsonPath("$.email").value(updatedEmail));
        return updateResult;
    }

    private void testInternalAccountPasswordUpdate(String updatedPassword, String accountId, String accessToken) throws Exception {
        // Prepare the request payload
        String emailUpdateRequestJson = "{ \"new_plaintext_password\": \"" + updatedPassword + "\" }";

        // Perform the PUT request with Bearer Token Authentication
        ResultActions updateResult = mockMvc.perform(MockMvcRequestBuilders.put("/accounts/{accountId}/update-internal-password", accountId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .content(emailUpdateRequestJson));

        // Validate the response
        updateResult.andExpect(status().isOk());
    }

    private void testInternalAccountDeletion(String accountId, String accessToken) throws Exception {

        // Perform the PUT request with Bearer Token Authentication
        ResultActions deletionResult = mockMvc.perform(MockMvcRequestBuilders.delete("/accounts/{accountId}", accountId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken));

        // Validate the response
        deletionResult.andExpect(status().isOk());
    }

    private void validateInternalAccountJwt(String accountId, String accessToken) throws JsonProcessingException {

        Jwt jwt = jwtDecoder.decode(accessToken);

        assertEquals("account", jwt.getClaimAsString("subject-type"));
        assertEquals(accountId, jwt.getClaimAsString("sub"));
        assertEquals(false, jwt.getClaim("verified"));
    }

    @Test
    public void internalAccountLifecycle() throws Exception {

        // Prepare the Basic Authentication credentials
        String email = "test@example.com";
        String password = "P@ssw0rd";


        System.out.println("Register internal account");
        testInternalAccountRegistration(email, password);


        System.out.println("Login internal account");
        ResultActions loginResult = testInternalAccountLogin(email, password);
        String accountId = JsonPath.read(loginResult.andReturn().getResponse().getContentAsString(), "$.account.id");
        String accessToken = JsonPath.read(loginResult.andReturn().getResponse().getContentAsString(), "$.access_token");
        validateInternalAccountJwt(accountId, accessToken);


        System.out.println("Register internal account wrong password");
        String wrongPassword = "Wr0ngP@ssw0rd";
        assertEquals(
                "Response status expected:<200> but was:<401>",
                assertThrows(AssertionError.class, () -> testInternalAccountLogin(email, wrongPassword)).getMessage()
        );


        System.out.println("Update internal account email");
        String updatedEmail = "test@updated";
        testInternalAccountEmailUpdate(updatedEmail, accountId, accessToken);
        ResultActions updatedLoginResult = testInternalAccountLogin(updatedEmail, password);
        String updatedAccessToken = JsonPath.read(updatedLoginResult.andReturn().getResponse().getContentAsString(), "$.access_token");
        validateInternalAccountJwt(accountId, updatedAccessToken);


        System.out.println("Login with old email");
        assertEquals(
                "Response status expected:<200> but was:<401>",
                assertThrows(AssertionError.class, () -> testInternalAccountLogin(email, password)).getMessage()
        );


        System.out.println("Update internal account password");
        String updatePassword = "N€wP@ssw0rd";
        testInternalAccountPasswordUpdate(updatePassword, accountId, updatedAccessToken);
        testInternalAccountLogin(updatedEmail, updatePassword);


        System.out.println("Login with old password");
        assertEquals(
                "Response status expected:<200> but was:<401>",
                assertThrows(AssertionError.class, () -> testInternalAccountLogin(updatedEmail, password)).getMessage()
        );


        System.out.println("Delete internal account");
        testInternalAccountDeletion(accountId, updatedAccessToken);


        System.out.println("Login with deleted account");
        assertEquals(
                "Response status expected:<200> but was:<401>",
                assertThrows(AssertionError.class, () -> testInternalAccountLogin(updatedEmail, updatePassword)).getMessage()
        );
    }



    private ResultActions testClientCreation(String name, String [] audiences, String [] scopes) throws Exception {
        // Prepare the request payload
        String creationRequestJson =
                "{ " +
                        "\"name\": \"" + name + "\"," +
                        "\"audiences\": " + objectMapper.writeValueAsString(audiences) + "," +
                        "\"scopes\": " + objectMapper.writeValueAsString(scopes) +
                " }";

        // Perform the POST request
        ResultActions creationResult = mockMvc.perform(MockMvcRequestBuilders.post("/clients/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(creationRequestJson));

        // Validate the response
        creationResult.andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.password").isNotEmpty())
                .andExpect(jsonPath("$.audiences").value(Matchers.contains(audiences)))
                .andExpect(jsonPath("$.scopes").value(Matchers.contains(scopes)));
        return creationResult;
    }

    private ResultActions testClientLogin(String name, String password) throws Exception {
        // Perform the POST request with Basic Authentication
        ResultActions loginResult = mockMvc.perform(MockMvcRequestBuilders.post("/oauth2/token")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("grant_type", "client_credentials")
                .header("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString((name + ":" + password).getBytes())));

        System.out.println(loginResult);

        loginResult.andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON + ";charset=UTF-8"))
                .andExpect(jsonPath("$.access_token").isNotEmpty());
        return loginResult;
    }


    private ResultActions testClientPasswordReset(String name, String accessToken, String [] audiences, String [] scopes) throws Exception {

        // Perform the PUT request with Bearer Token Authentication
        ResultActions resetResult = mockMvc.perform(MockMvcRequestBuilders.put("/clients/{clientId}/reset-password", name)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken));

        // Validate the response
        resetResult.andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.password").isNotEmpty())
                .andExpect(jsonPath("$.audiences").value(Matchers.contains(audiences)))
                .andExpect(jsonPath("$.scopes").value(Matchers.contains(scopes)));
        return resetResult;
    }

    private void testClientDeletion(String name, String accessToken) throws Exception {

        // Perform the PUT request with Bearer Token Authentication
        ResultActions deletionResult = mockMvc.perform(MockMvcRequestBuilders.delete("/clients/{clientId}", name)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken));

        // Validate the response
        deletionResult.andExpect(status().isOk());
    }

    private void validateClientJwt(String name, String [] audiences, String [] scopes, String accessToken) throws JsonProcessingException {

        Jwt jwt = jwtDecoder.decode(accessToken);

        assertEquals("client", jwt.getClaimAsString("subject-type"));
        assertEquals(name, jwt.getClaimAsString("sub"));
        assertEquals(Arrays.asList(audiences), jwt.getClaim("aud"));
        assertEquals(Arrays.asList(scopes), jwt.getClaim("scope"));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    public void clientLifecycle() throws Exception {

        // Prepare the Basic Authentication credentials
        String name = "testclient";
        String [] audiences = { "service1", "service2" };
        String [] scopes =  { "canDoA", "canDoB" };


        System.out.println("Client Creation");

        ResultActions creationResult = testClientCreation(name, audiences, scopes);
        String password = JsonPath.read(creationResult.andReturn().getResponse().getContentAsString(), "$.password");

        System.out.println("Login with generated PW");

        ResultActions loginResult = testClientLogin(name, password);
        String accessToken = JsonPath.read(loginResult.andReturn().getResponse().getContentAsString(), "$.access_token");
        validateClientJwt(name, audiences, scopes, accessToken);


        /* Idempotence: These tests sometimes fail for no reason (related to Spring oauth2 and embedded mongodb) */

        System.out.println("Login with wrong PW");

        String wrongPassword = "Wr0ngP@ssw0rd";
        assertEquals(
                "Response status expected:<200> but was:<401>",
                assertThrows(AssertionError.class, () -> testClientLogin(name, wrongPassword)).getMessage()
        );


        System.out.println("PW reset");

        ResultActions resetResult = testClientPasswordReset(name, accessToken, audiences, scopes);
        String resetPassword = JsonPath.read(resetResult.andReturn().getResponse().getContentAsString(), "$.password");

        TimeUnit.SECONDS.sleep(5); // thanks to oauth2 & embedded mongodb


        System.out.println("Login with old PW");

        assertEquals(
                "Response status expected:<200> but was:<401>",
                assertThrows(AssertionError.class, () -> testClientLogin(name, password)).getMessage()
        );


        System.out.println("Login with reset PW");

        testClientLogin(name, resetPassword);


        System.out.println("Client Deletion");

        testClientDeletion(name, accessToken);

        TimeUnit.SECONDS.sleep(5); // thanks to oauth2 & embedded mongodb

        System.out.println("Login after Deletion");

        assertEquals(
                "Response status expected:<200> but was:<401>",
                assertThrows(AssertionError.class, () -> testClientLogin(name, password)).getMessage()
        );
    }
}