package thi.cnd.careerservice.shared.view;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

import jakarta.annotation.Nonnull;

public interface PaginationService {
    @Nonnull
    Pageable createPageableOfRequest(Optional<Integer> skip, Optional<Integer> limit);
}
