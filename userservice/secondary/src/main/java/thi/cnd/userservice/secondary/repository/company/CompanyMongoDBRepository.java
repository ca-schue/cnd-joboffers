package thi.cnd.userservice.secondary.repository.company;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import thi.cnd.userservice.core.model.company.CompanyId;
import thi.cnd.userservice.secondary.repository.company.model.CompanyDAO;

import java.util.Optional;
import java.util.Set;

@Repository
public interface CompanyMongoDBRepository extends MongoRepository<CompanyDAO, CompanyId> {

    Page<CompanyDAO> findByDetailsNameContainingIgnoreCaseAndDetailsTagsContainingIgnoreCase(String name, Set<String> details_tags,
                                                                                             Pageable pageable);

    Optional<CompanyDAO> deleteOneById(CompanyId id);
}
