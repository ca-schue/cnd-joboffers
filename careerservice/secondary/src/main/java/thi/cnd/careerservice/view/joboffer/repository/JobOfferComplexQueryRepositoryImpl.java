package thi.cnd.careerservice.view.joboffer.repository;

import java.util.List;
import java.util.function.LongSupplier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.joboffer.model.JobOfferStatus;
import thi.cnd.careerservice.joboffer.view.model.SearchAvailableJobOfferFilter;
import thi.cnd.careerservice.view.joboffer.model.JobOfferViewDAO;
import lombok.RequiredArgsConstructor;

import static thi.cnd.careerservice.joboffer.model.JobOfferStatus.OPEN;

@Repository
@RequiredArgsConstructor
public class JobOfferComplexQueryRepositoryImpl implements JobOfferComplexQueryRepository {

    private final MongoTemplate mongoTemplate;

    private static final Class<JobOfferViewDAO> CLASS = JobOfferViewDAO.class;

    @Override
    public Page<JobOfferViewDAO> findAllOpen(SearchAvailableJobOfferFilter filter) {
        var query = new Query().addCriteria(byStatus(OPEN));
        filter.title().ifPresent(title -> query.addCriteria(Criteria.where("title").regex(title, "i")));
        if (!filter.companyIds().isEmpty()) {
            query.addCriteria(Criteria.where("companyId").in(filter.companyIds()));
        }
        return findPaginated(query, filter.pageable(), CLASS);
    }

    @Override
    public List<JobOfferViewDAO> findByCompanyId(CompanyId companyId, boolean showOnlyOpen) {
        return mongoTemplate.find(buildQueryByCompanyId(companyId, showOnlyOpen), CLASS);
    }

    @Override
    public long countByCompanyId(CompanyId companyId, boolean showOnlyOpen) {
        return mongoTemplate.count(buildQueryByCompanyId(companyId, showOnlyOpen), CLASS);
    }

    private Query buildQueryByCompanyId(CompanyId companyId, boolean showOnlyOpen) {
        var query = new Query().addCriteria(Criteria.where("companyId").is(companyId));

        if (showOnlyOpen) {
            query = query.addCriteria(byStatus(OPEN));
        }

        return query;
    }

    private Criteria byStatus(JobOfferStatus status) {
        return Criteria.where("status").is(status);
    }

    private <T> Page<T> findPaginated(Query query, Pageable pagination, Class<T> clazz) {
        var totalItemCount = mongoTemplate.count(query, clazz);
        var paginationQuery = applyPaginationToQuery(query, pagination);
        List<T> items = mongoTemplate.find(paginationQuery, clazz);
        return buildPage(pagination, items, totalItemCount);
    }

    private Query applyPaginationToQuery(Query query, Pageable pagination) {
        return query
            .limit(pagination.getPageSize())
            .skip((long) pagination.getPageNumber() * pagination.getPageSize())
            .with(pagination.getSort());
    }

    private <T> Page<T> buildPage(Pageable pagination, List<T> foundItems, long totalItemCount) {
        LongSupplier totalCountSupplier = () -> totalItemCount;
        return PageableExecutionUtils.getPage(foundItems, pagination, totalCountSupplier);
    }

}
