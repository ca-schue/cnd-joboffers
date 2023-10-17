package thi.cnd.authservice.primary.rest.controllers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import thi.cnd.authservice.core.exceptions.AccountAlreadyExistsException;
import thi.cnd.authservice.core.exceptions.InvalidPasswordException;
import thi.cnd.authservice.core.model.Account;
import thi.cnd.authservice.core.ports.primary.AccountServicePort;

@RestController
@AllArgsConstructor
public class TestController {

    private final AccountServicePort accountServicePort;

    @PostMapping("/accounts/loginInternalAccount2")
    public String test() {
        return "test works";
    }

    @PostMapping("/accounts/test")
    public String test2(@RequestBody String req) {
        return "test POST works; req: " + req;
    }

    @GetMapping("/test")
    public String test3(){
        return "test3 works";
    }

    @GetMapping("/accounts/test4")
    public String test4(){
        return "test4 works";
    }

    @Getter
    public static class AccountTestDTO {
        private String email;
        private String password;
    }

    @PostMapping("/accountCreationTest")
    public String test3(@RequestBody AccountTestDTO account){
        return "accountCreationTest POST works: " + account.getEmail() + ", " + account.getPassword();
    }

    @PostMapping("/accountCreationTest2")
    public ResponseEntity<String> test5(@RequestBody AccountTestDTO accountTestDTO){
        Account account;
        try {
            account = accountServicePort.registerNewInternalAccount(accountTestDTO.getEmail(), accountTestDTO.getPassword());
        } catch (AccountAlreadyExistsException | InvalidPasswordException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        }
        return ResponseEntity.ok("Created");
    }
}