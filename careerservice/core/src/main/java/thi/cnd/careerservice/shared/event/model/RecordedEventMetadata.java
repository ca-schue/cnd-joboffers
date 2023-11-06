package thi.cnd.careerservice.shared.event.model;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

/**
 * Contains metadata of a past event that was already persisted
 */
@Validated
public record RecordedEventMetadata(
    @NotBlank String id,
    long streamPosition,
    long logPosition,
    @NotBlank String type
) {
}
