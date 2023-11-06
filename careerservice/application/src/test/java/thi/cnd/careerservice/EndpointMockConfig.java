package thi.cnd.careerservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.Response;
import com.github.tomakehurst.wiremock.matching.UrlPattern;

import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.setup.mock.endpoint.FunctionalResponseTransformer;
import thi.cnd.careerservice.setup.mock.endpoint.RequestResponseTransformerFunction;
import thi.cnd.careerservice.user.model.UserId;
import thi.cnd.careerservice.shared.UserServiceTestProvider;

@Component
@Profile("integrationtest")
public class EndpointMockConfig {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private WireMockServer wireMockServer;

    @Autowired
    private ObjectMapper objectMapper;

    public void mockCompanyResponse(CompanyId companyId, UserId userId) {
        mockGetEndpoint(COMPANY_URL, UserServiceTestProvider.buildCompanyResponse(companyId.getId(), userId.getId()));
    }

    public void mockUserResponse(UserId userId) {
        mockGetEndpoint(USER_URL, UserServiceTestProvider.buildUserResponse(userId.getId()));
    }

    private void mockGetEndpoint(UrlPattern pattern, Object response) {
        mockGetEndpoint(pattern, request -> {
            try {
                return Response.response()
                    .body(objectMapper.writeValueAsString(response).getBytes())
                    .status(HttpStatus.OK.value())
                    .headers(new HttpHeaders(JSON_HEADER))
                    .build();
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Could not deserialize mocked endpoint response", e);
            }
        });
    }

    private void mockGetEndpoint(UrlPattern pattern, RequestResponseTransformerFunction mapping) {
        var mappingBuilder = WireMock.get(pattern).willReturn(
            WireMock.aResponse().withHeaders(new HttpHeaders(JSON_HEADER)).withTransformer(
                FunctionalResponseTransformer.class.getSimpleName(),
                RequestResponseTransformerFunction.RESPONSE_TRANSFORMER_FUNCTION,
                mapping
            )
        );
        wireMockServer.stubFor(mappingBuilder);
    }

    public void resetMocksToDefault() {
        mockCompanyResponse(new CompanyId(), new UserId());
        mockUserResponse(new UserId());
    }

    private static final UrlPattern COMPANY_URL = WireMock.urlPathTemplate("/user-service/companies/{companyId}");
    private static final UrlPattern USER_URL = WireMock.urlPathTemplate("/user-service/users/{userId}");
    private static final HttpHeader JSON_HEADER = new HttpHeader("Content-Type", "application/json");

}
