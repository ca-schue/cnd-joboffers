package thi.cnd.careerservice.shared;

import javax.money.MonetaryAmount;

import thi.cnd.careerservice.test.api.generated.model.MoneyDTO;

public class ApiMapper {

    private ApiMapper() {}

    public static MoneyDTO toMoneyDTO(MonetaryAmount amount) {
        return toMoneyDTO(amount.getNumber().floatValue(), amount.getCurrency().getCurrencyCode());
    }

    public static MoneyDTO toMoneyDTO(float value, String currency) {
        return new MoneyDTO().amount(value).currency(currency);
    }


}
