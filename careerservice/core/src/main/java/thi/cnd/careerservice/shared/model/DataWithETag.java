package thi.cnd.careerservice.shared.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Used as return value for the command side of the application.
 */
@Getter
@AllArgsConstructor
public class DataWithETag<T> {

    private final ETag eTag;
    private final T data;

}
