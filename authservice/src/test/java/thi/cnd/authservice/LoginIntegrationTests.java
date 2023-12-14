package thi.cnd.authservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataMongo
public class LoginIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testLoginIntegration() throws Exception{
        // Prepare the request payload
        String requestJson = "{ \"email\": \"test@example.com\", \"password\": \"P@ssw0rd\" }";

        // Perform the POST request
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/accounts/registerInternalAccount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        // Validate the response
        result.andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
                /*.andExpect(jsonPath("$.account.accountType").value("internal"))
                .andExpect(jsonPath("$.account.id").value("string"))
                .andExpect(jsonPath("$.account.email").value("string"))
                .andExpect(jsonPath("$.access_token").value("string"));*/
    }
}