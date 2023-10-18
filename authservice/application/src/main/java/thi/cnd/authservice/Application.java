package thi.cnd.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import thi.cnd.authservice.core.exceptions.AccountAlreadyExistsException;
import thi.cnd.authservice.core.model.Account;
import thi.cnd.authservice.core.model.AccountId;
import thi.cnd.authservice.core.model.AccountProvider;
import thi.cnd.authservice.secondary.repository.mongodb.account.AccountMongoDBRepository;
import thi.cnd.authservice.secondary.repository.mongodb.account.AccountRepositoryAdapter;

import java.time.Instant;


@SpringBootApplication
@EnableConfigurationProperties
@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
@ComponentScan(basePackages = {"thi.cnd.authservice.api", "thi.cnd.authservice.core", "thi.cnd.authservice.primary", "thi.cnd.authservice.secondary"})
@ConfigurationPropertiesScan
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        /*Account account = new Account(new AccountId(), AccountProvider.INTERNAL, "email", "pw", Instant.now());
        // Debug: ((AnnotationConfigServletWebServerApplicationContext) context).beanFactory.allBeanNamesByType.values().stream().map(strings -> Arrays.stream(strings).filter(string -> string.contains("client")).collect(Collectors.toList())).collect(Collectors.toSet())
        try {
            context.getBean(AccountRepositoryAdapter.class).save(account);
        } catch (AccountAlreadyExistsException e) {
            throw new RuntimeException("Errorrrr!");
        }*/
    }

}
