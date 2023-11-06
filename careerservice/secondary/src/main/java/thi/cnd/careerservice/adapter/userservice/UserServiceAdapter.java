package thi.cnd.careerservice.adapter.userservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import thi.cnd.careerservice.api.ApiClient;
import thi.cnd.careerservice.api.generated.CompanyApi;
import thi.cnd.careerservice.api.generated.UserApi;
import thi.cnd.careerservice.company.model.Company;
import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.company.CompanyPort;
import thi.cnd.careerservice.exception.ResourceNotFoundException;
import thi.cnd.careerservice.user.model.User;
import thi.cnd.careerservice.user.model.UserId;
import thi.cnd.careerservice.user.UserPort;

/**
 * Adapter that calls the user-service for fetching {@link User} data
 */
@Component
public class UserServiceAdapter implements UserPort, CompanyPort {

    private final UserApi userApi;
    private final CompanyApi companyApi;

    public UserServiceAdapter(WebClient webClient, @Value("${endpoints.user-service}") String userServiceBasePath) {
        var userServiceApiClient = new ApiClient(webClient).setBasePath(userServiceBasePath);
        this.userApi = new UserApi(userServiceApiClient);
        this.companyApi = new CompanyApi(userServiceApiClient);
    }

    @Override
    public User getUser(UserId userId) {
        try {
            var response = userApi.getUser(userId.getId()).block();

            validateResponseNotEmpty(response, "User response from user-service is empty.");

            return UserServiceApiMapper.toUser(response);

        } catch (WebClientResponseException e) {
            handleNotFoundError(e, "User was not found for id " + userId);
            throw e;
        }
    }

    @Override
    public Company getCompany(CompanyId companyId) {
        try {
            var response = companyApi.getCompany(companyId.getId()).block();

            validateResponseNotEmpty(response, "Company response from user-service is empty.");

            return UserServiceApiMapper.toCompany(response);

        } catch (WebClientResponseException e) {
            handleNotFoundError(e, "Company was not found for id " + companyId);
            throw e;
        }
    }

    private void validateResponseNotEmpty(Object response, String message) {
        if (response == null) {
            throw new IllegalStateException(message);
        }
    }

    private void handleNotFoundError(WebClientResponseException e, String message) {
        if (e.getStatusCode().value() == HttpStatus.NOT_FOUND.value()) {
            throw new ResourceNotFoundException(message);
        }
    }
}
