package thi.cnd.userservice.adapters.out.repository.company;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import thi.cnd.userservice.adapters.out.repository.company.DAOs.CompanyDAO;
import thi.cnd.userservice.domain.model.company.CompanyId;
import thi.cnd.userservice.domain.model.user.UserId;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CustomCompanyMongoRepository extends MongoRepository<CompanyDAO, CompanyId> {

    Page<CompanyDAO> findByDetailsNameContainingIgnoreCaseAndDetailsTagsContainingIgnoreCase(String name, Set<String> details_tags,
                                                                                             Pageable pageable);

    List<CompanyDAO> findByMembersContains(UserId userId);

    Optional<CompanyDAO> deleteOneById(CompanyId id);
}
