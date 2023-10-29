package thi.cnd.careerservice.shared.view.model;

import lombok.Getter;

@Getter
public class PaginatedView {

    private final int skip;
    private final int limit;

    public PaginatedView(
        int skip,
        int limit,
        int maxLimit
    ) {
        this.skip = Math.max(skip, 0);
        this.limit = limit <= 0 || limit >= maxLimit ? maxLimit : limit;
    }

}
