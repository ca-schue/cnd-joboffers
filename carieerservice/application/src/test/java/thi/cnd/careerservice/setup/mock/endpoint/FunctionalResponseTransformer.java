package thi.cnd.careerservice.setup.mock.endpoint;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.extension.ResponseTransformer;
import com.github.tomakehurst.wiremock.extension.ResponseTransformerV2;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.Response;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;

/**
 * A {@link ResponseTransformer}, which is applied to the {@link WireMockServer} created by the {@link WireMockInitializer}. It enables the
 * user to dynamically create a {@link Response} while taking the incoming {@link Request} into consideration.
 *
 * @see RequestResponseTransformerFunction
 */
public class FunctionalResponseTransformer implements ResponseTransformerV2 {

    private final boolean globally;

    FunctionalResponseTransformer(boolean globally) {
        this.globally = globally;
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public Response transform(Response response, ServeEvent serveEvent) {
        RequestResponseTransformerFunction responseTransformerFunction = getRequestResponseTransformerFunction(serveEvent);

        try {
            return responseTransformerFunction.transform(serveEvent.getRequest());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not transform request to mocked response", e);
        }
    }

    @NotNull
    private static RequestResponseTransformerFunction getRequestResponseTransformerFunction(ServeEvent serveEvent) {
        Object responseTransformerFunctionObj =
            Objects.requireNonNull(
                serveEvent.getTransformerParameters().get(RequestResponseTransformerFunction.RESPONSE_TRANSFORMER_FUNCTION),
                "Paramters does not contain ResponseTransformerFunction"
            );

        if (!(responseTransformerFunctionObj instanceof RequestResponseTransformerFunction responseTransformerFunction)) {
            throw new IllegalStateException("The ResponseTransformerFunction parameter must be an instance of "
                + RequestResponseTransformerFunction.class.getSimpleName());
        }

        return responseTransformerFunction;
    }

    @Override
    public boolean applyGlobally() {
        return globally;
    }
}
