package thi.cnd.authservice.core.domain;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class PasswordEncoder extends BCryptPasswordEncoder {
}
