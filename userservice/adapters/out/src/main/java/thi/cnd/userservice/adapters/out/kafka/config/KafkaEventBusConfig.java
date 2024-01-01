package thi.cnd.userservice.adapters.out.kafka.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaProducerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.jackson.datatype.money.MoneyModule;

import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class KafkaEventBusConfig implements BeanFactoryAware {

    private BeanFactory beanFactory;
    private final KafkaTopicConfig kafkaTopicConfig;

    @Bean("KafkaObjectMapper")
    ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .registerModule(new MoneyModule())
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Bean
    DefaultKafkaProducerFactoryCustomizer createPayloadSerializer(@Qualifier("KafkaObjectMapper") ObjectMapper objectMapper) {
        return producerFactory -> {
            if (Objects.nonNull(producerFactory)) {
                producerFactory.setValueSerializer(new CustomJsonStringSerializer<>(objectMapper));
            }
        };
    }

    @Override
    public void setBeanFactory(@Nonnull BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @PostConstruct
    public void onPostConstruct() {
        kafkaTopicConfig.getTopics().forEach((key, value) ->
                ((ConfigurableBeanFactory) beanFactory).registerSingleton(value.name(), value.toTopic())
        );
    }
}