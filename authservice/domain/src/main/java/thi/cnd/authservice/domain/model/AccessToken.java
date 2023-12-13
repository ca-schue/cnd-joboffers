package thi.cnd.authservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;

@AllArgsConstructor
@Getter
public class AccessToken {
    String tokenValue;
    Instant issuedAt;
    Instant expiresAt;
    Map<String, Object> headers;
    Map<String, Object> claims;
}
