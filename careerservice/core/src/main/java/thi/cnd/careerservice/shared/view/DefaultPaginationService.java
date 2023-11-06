package thi.cnd.careerservice.shared.view;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.annotation.Nonnull;

@Service
public class DefaultPaginationService implements PaginationService {

    private final int defaultFallbackPaginationLimit;
    private final int defaultMaxLimitForPagination;

    public DefaultPaginationService(
        @Value("${global.defaultFallbackPaginationLimit}") int defaultFallbackPaginationLimit,
        @Value("${global.defaultMaxLimitForPagination}") int defaultMaxLimitForPagination
    ) {
        this.defaultFallbackPaginationLimit = defaultFallbackPaginationLimit;
        this.defaultMaxLimitForPagination = defaultMaxLimitForPagination;
    }

    @Override
    @Nonnull
    public Pageable createPageableOfRequest(Optional<Integer> skip, Optional<Integer> limit) {
        return PageRequest.of(
            skip.orElse(0),
            Math.min(limit.orElse(defaultFallbackPaginationLimit), defaultMaxLimitForPagination)
        );
    }

}
