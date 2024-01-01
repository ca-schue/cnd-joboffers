package thi.cnd.userservice.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import thi.cnd.userservice.application.ports.out.event.CompanyEvents;
import thi.cnd.userservice.application.ports.out.event.UserEvents;
import thi.cnd.userservice.application.ports.out.repository.CompanyRepository;
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
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceUnitTests {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CompanyEvents companyEvents;

    @InjectMocks
    private CompanyServiceImpl companyService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    public void testDeleteUserFromAllCompanies() {
        UserId userTbd = new UserId(UUID.randomUUID());

        // Mocked data
        UserId ownerId1 = new UserId(UUID.randomUUID());
        CompanyDetails companyDetails1 = new CompanyDetails("companyName1", "some description1", Set.of("tag11", "tag21"), "location1");
        CompanyLinks companyLinks1 = new CompanyLinks("www.company1.com", Set.of("x/comp1", "linkedin/comp1"));
        Company company1 = new Company(ownerId1, companyDetails1, companyLinks1);
        company1.setMembers(new HashSet<>());
        company1.getMembers().add(userTbd);

        UserId ownerId2 = new UserId(UUID.randomUUID());
        CompanyDetails companyDetails2 = new CompanyDetails("companyName2", "some description2", Set.of("tag12", "tag22"), "location2");
        CompanyLinks companyLinks2 = new CompanyLinks("www.company2.com", Set.of("x/comp2", "linkedin/comp2"));
        Company company2 = new Company(ownerId2, companyDetails2, companyLinks2);
        company2.setMembers(new HashSet<>());
        company2.getMembers().add(userTbd);

        List<Company> companyList = List.of(company1, company2);

        // Mocking behavior
        when(companyRepository.findAllCompaniesWithUserMember(userTbd)).thenReturn(companyList);

        // Call the method to be tested
        companyService.deleteUserFromAllCompanies(userTbd);

        // Verify interactions and expectations
        verify(companyRepository, times(2)).updateOrSaveCompany(any()); // Called twice, once for each company

        // Assertions
        assertFalse(company1.getMembers().contains(userTbd));
        assertFalse(company2.getMembers().contains(userTbd));
    }

}
