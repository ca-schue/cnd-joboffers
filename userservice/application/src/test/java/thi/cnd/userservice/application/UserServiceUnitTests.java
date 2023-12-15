package thi.cnd.userservice.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import thi.cnd.userservice.application.ports.out.event.UserEvents;
import thi.cnd.userservice.application.ports.out.repository.UserRepository;
import thi.cnd.userservice.domain.CompanyService;
import thi.cnd.userservice.domain.exceptions.CompanyNotFoundByIdException;
import thi.cnd.userservice.domain.exceptions.UserAlreadyMemberOfCompanyException;
import thi.cnd.userservice.domain.exceptions.UserNotFoundByEmailException;
import thi.cnd.userservice.domain.model.company.Company;
import thi.cnd.userservice.domain.model.company.CompanyDetails;
import thi.cnd.userservice.domain.model.company.CompanyId;
import thi.cnd.userservice.domain.model.company.CompanyLinks;
import thi.cnd.userservice.domain.model.user.User;
import thi.cnd.userservice.domain.model.user.UserCompanyAssociation;
import thi.cnd.userservice.domain.model.user.UserId;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CompanyService companyService;

    @Mock
    private UserEvents userEvents;

    @InjectMocks
    private UserServiceImpl userService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    public void testInviteUserToCompany() throws UserNotFoundByEmailException, CompanyNotFoundByIdException, UserAlreadyMemberOfCompanyException {
        // Mocked data
        CompanyId companyId = new CompanyId(UUID.randomUUID());
        String userEmail = "user@example.com";
        String firstName = "firstName";
        String lastName = "lastName";
        UserId userId = new UserId(UUID.randomUUID());
        User user = new User(userId, userEmail, firstName, lastName);
        user.setAssociations(new UserCompanyAssociation(new HashSet<>(), new HashSet<>(), null));

        UserId ownerId = new UserId(UUID.randomUUID());
        CompanyDetails companyDetails = new CompanyDetails("companyName", "some description", Set.of("tag1", "tag2"), "location");
        CompanyLinks companyLinks = new CompanyLinks("www.company.com", Set.of("x/comp", "linkedin/comp"));
        Company company = new Company(ownerId, companyDetails, companyLinks);

        // Mocking behavior
        when(companyService.findCompanyById(companyId)).thenReturn(company);
        when(userRepository.findUserByEmail(userEmail)).thenReturn(user);

        // Call the method to be tested
        userService.inviteUserToCompany(companyId, userEmail);

        // Verify interactions and expectations
        verify(userRepository).updateOrSaveUser(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        // Assertions
        assertTrue(capturedUser.getAssociations().getInvitedTo().contains(companyId));

    }

    // updateUserProfile

    // deleteUser => assert that mocked Repository is called !

    // inviteUserToCompany => assert that repository receives user with invitation

    // acceptCompanyInvitation => assert that repository receives user with member and without invitation

}
