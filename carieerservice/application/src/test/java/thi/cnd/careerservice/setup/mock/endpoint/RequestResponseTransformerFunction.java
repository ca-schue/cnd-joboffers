package thi.cnd.careerservice.setup.mock.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.Response;

/**
 * In case the {@link Response} depends on the input of the {@link Request} you can pass this {@link RequestResponseTransformerFunction} to
 * the wiremock stub to create a custom response.
 */
@FunctionalInterface
public interface RequestResponseTransformerFunction {

    /**
     * Name of the parameter used by the {@link FunctionalResponseTransformer}
     */
    String RESPONSE_TRANSFORMER_FUNCTION = "ResponseTransformerFunction";

    /**
     * A function, which is capable to dynamically create a {@link Response} depending on the incoming request.
     *
     * @param  request which comes in from the client
     * @return         a {@link Response}, which can take the incoming request into consideration
     */
    Response transform(Request request) throws JsonProcessingException;
}
