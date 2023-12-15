package thi.cnd.authservice.adapters.out.security.jwt;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import thi.cnd.authservice.domain.model.AccessToken;
import thi.cnd.authservice.domain.model.account.Account;
import thi.cnd.authservice.domain.model.account.AccountId;
import thi.cnd.authservice.domain.model.account.InternalAccount;

import java.time.Instant;
import java.util.Base64;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {JwtProvider.class, JwtClaims.class, JwkSet.class})
public class JwtSliceTest {

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    JwtClaims jwtClaims;

    @Autowired
    JwkSet jwkSet;

    @Test
    public void jwtTests (){

        AccountId id = new AccountId();
        Instant lastLogin = Instant.now();
        String email = "some@mail";
        String encryptedPassword = Base64.getEncoder().encodeToString("S0meP@ssword".getBytes());
        boolean verified = true;

        Account account = new InternalAccount(id, lastLogin, email, encryptedPassword, verified);
        AccessToken accessToken = jwtProvider.createAccountAccessToken(account);
        Map<String, Object> claims = accessToken.getClaims();
        assertEquals(id.toString(), claims.get("sub"));
    }

}
