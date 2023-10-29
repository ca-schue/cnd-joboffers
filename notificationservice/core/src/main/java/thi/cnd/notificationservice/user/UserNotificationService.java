package thi.cnd.notificationservice.user;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Validated
public interface UserNotificationService {

    void sendWelcomeEmail(@Email String email, @NotBlank String firstname);

    void sendEmailOnCompanyRegistration(@Email String email, @NotBlank String companyName);

    void sendEmailOnUserInvitationToCompany(String userEmail, String userFirstName, String companyName);
}
