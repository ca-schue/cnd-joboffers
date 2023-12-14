/*package integration;

public class InternalAccountLoginIntegrationTests {
    // TODO: Test every login method simply on controller-level?

    // TODO: Testing on filter-chain-level:
    // TODO: "/accounts/loginInternalAccount"
    //  - access denied with everything except Basic Auth
    //  - wrong/non-existent credentials: check if access denied
    //  - correct credentials:
    //      > InternalAccountDetailsService calls "AccountRepositoryPort.findInternalAccountByEmailAndUpdateLastLogin()"
    //      > Account is loaded into InternalAccountDetails object -> SecurityContextHolder
    //      > "loginInternalAccount()" method is called in account controller (mocked)

    // TODO: needs to be injected:
    //  - DaoAuthenticationProvider
    //  - InternalAccountDetailsService
    // TODO: needs to be mocked:
    //  - AccountController.loginInternalAccount()
    //  - AccountRepositoryPort.findInternalAccountByEmailAndUpdateLastLogin()

}*/
