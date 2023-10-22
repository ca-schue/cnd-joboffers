package thi.cnd.authservice.core.domain.password;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class PasswordEncoder extends BCryptPasswordEncoder {
}
