package thi.cnd.careerservice;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.test.context.TestPropertySource;

import thi.cnd.careerservice.setup.mock.query.QueryPortTestConfig;

/**
 * Mocks all qu
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@TestPropertySource(properties = {
    QueryPortTestConfig.MOCK_JOB_OFFER_QUERY_PORT_PROPERTY + "=true",
    QueryPortTestConfig.MOCK_JOB_APPLICATION_QUERY_PORT_PROPERTY + "=true",
})
public @interface MockQueryPorts {

}
