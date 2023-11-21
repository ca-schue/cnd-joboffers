package thi.cnd.authservice.domain.model.account;

public enum AccountProvider {
    INTERNAL("INTERNAL"),
    OIDC("OIDC");

    public final String name;

    private AccountProvider(String name) {
        this.name = name;
    }
}
