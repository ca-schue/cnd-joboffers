package thi.cnd.notificationservice.user;

import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;
import thi.cnd.notificationservice.EmailNotifier;

@Service
@RequiredArgsConstructor
public class DefaultUserNotificationService implements UserNotificationService {

    private final EmailNotifier emailNotifier;

    @Override
    public void sendWelcomeEmail(String email, String firstname) {
        var content = "Welcome " + firstname + "! I hope you will have a nice stay.";
        emailNotifier.sendEmailToCustomer(email, content);
    }

    @Override
    public void sendEmailOnCompanyRegistration(String email, String companyName) {
        emailNotifier.sendEmailToCustomer(email, "Congratulation on creating your company " + companyName);
    }

    @Override
    public void sendEmailOnUserInvitationToCompany(String userEmail, String userFirstName, String companyName) {
        emailNotifier.sendEmailToCustomer(userEmail, "Hello " + userEmail + ", you have been invited to join " + companyName + ".");
    }


}
