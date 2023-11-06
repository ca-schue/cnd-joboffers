package thi.cnd.careerservice.shared.view.model;

import thi.cnd.careerservice.shared.model.ETag;
import thi.cnd.careerservice.shared.event.model.RecordedEventMetadata;

/**
 * Metadata that is saved alongside the {@link View} to check for the {@link ETag} version and optimistic locking mechanism
 */
public record ViewEventMetadata(
    long version,
    long lastAppliedLogPosition
) {

    public ViewEventMetadata(RecordedEventMetadata eventMetadata) {
        this(eventMetadata.streamPosition(), eventMetadata.logPosition());
    }

}
