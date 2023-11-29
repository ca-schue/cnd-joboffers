package thi.cnd.careerservice.shared.event.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.zalando.jackson.datatype.money.MoneyModule;

import com.eventstore.dbclient.ConnectionStringParsingException;
import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.EventStoreDBClientSettings;
import com.eventstore.dbclient.EventStoreDBConnectionString;
import com.eventstore.dbclient.EventStoreDBPersistentSubscriptionsClient;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class EventStoreConfig {

    @Bean(value = "EventStoreObjectMapper")
    ObjectMapper eventStoreObjectMapper() {
        return new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .registerModule(new MoneyModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Bean
    @Scope("singleton")
    EventStoreDBClient eventStoreDBClient(EventStoreDBClientSettings settings) {
        return EventStoreDBClient.create(settings);
    }

    @Bean
    @Scope("singleton")
    EventStoreDBPersistentSubscriptionsClient eventStoreDBPersistentSubscriptionsClient(EventStoreDBClientSettings settings) {
        return EventStoreDBPersistentSubscriptionsClient.create(settings);
    }

    @Bean
    EventStoreDBClientSettings eventStoreDBClientSettings(@Value("${event.event-store-uri}") String connectionString)
        throws ConnectionStringParsingException {
        return EventStoreDBConnectionString.parse(connectionString);
    }

}
