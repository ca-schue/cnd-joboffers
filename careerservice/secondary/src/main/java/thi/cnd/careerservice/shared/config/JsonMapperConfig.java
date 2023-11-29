package thi.cnd.careerservice.shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.zalando.jackson.datatype.money.MoneyModule;
import org.zalando.problem.jackson.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class JsonMapperConfig {

    @Bean
    @Primary
    ObjectMapper objectMapper() {
        return new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .registerModule(new MoneyModule())
            .registerModule(new ProblemModule())
            .registerModule(new ConstraintViolationProblemModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

}
