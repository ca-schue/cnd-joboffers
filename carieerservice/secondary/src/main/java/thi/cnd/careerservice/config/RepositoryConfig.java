package thi.cnd.careerservice.config;

import java.math.BigDecimal;
import java.util.List;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

import org.bson.Document;
import org.javamoney.moneta.Money;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

@Configuration
public class RepositoryConfig {

    private static final String NUMBER_DOCUMENT_KEY_NAME = "number";
    private static final String CURRENCY_DOCUMENT_KEY_NAME = "currency";

    public RepositoryConfig(MappingMongoConverter mappingMongoConverter) {
        configureMongoTypeMapping(mappingMongoConverter);
    }

    /**
     * Adds type mappings to the mongo client via {@link MappingMongoConverter}
     */
    private void configureMongoTypeMapping(MappingMongoConverter mappingMongoConverter) {
        mappingMongoConverter.setMapKeyDotReplacement("/");
        mappingMongoConverter.setCustomConversions(
            new MongoCustomConversions(
                List.of(
                    new MonetaryAmountWriteConverter(),
                    new MonetaryAmountReadConverter()
                )
            )
        );
        mappingMongoConverter.afterPropertiesSet();
    }

    /**
     * Write {@link MonetaryAmount} as {@link Document} into database.
     */
    private static class MonetaryAmountWriteConverter implements Converter<MonetaryAmount, Document> {

        @Override
        public Document convert(MonetaryAmount source) {
            Document document = new Document();
            document.put(NUMBER_DOCUMENT_KEY_NAME, source.getNumber().numberValueExact(BigDecimal.class).toString());
            document.put(CURRENCY_DOCUMENT_KEY_NAME, source.getCurrency().getCurrencyCode());
            return document;
        }
    }

    /**
     * Read {@link Document} as {@link MonetaryAmount} from database.
     */
    private static class MonetaryAmountReadConverter implements Converter<Document, MonetaryAmount> {

        @Override
        public MonetaryAmount convert(Document source) {
            CurrencyUnit currency = Monetary.getCurrency(source.getString(CURRENCY_DOCUMENT_KEY_NAME));
            BigDecimal number = new BigDecimal(source.getString(NUMBER_DOCUMENT_KEY_NAME));
            return Money.of(number, currency);
        }
    }

}
