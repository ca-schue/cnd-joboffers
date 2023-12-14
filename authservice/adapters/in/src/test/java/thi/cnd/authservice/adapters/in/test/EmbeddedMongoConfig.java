/*package thi.cnd.authservice.test.adapters.in;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class EmbeddedMongoConfig {

    @Bean
    public MongoClient mongoClient() {
        String mongoUri = "mongodb://localhost:27017/auth-service?authSource=admin&readPreference=primary&directConnection=true&ssl=false";

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(mongoUri))
                .build();

        return MongoClients.create(settings);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), "auth-service");
    }
}*/
