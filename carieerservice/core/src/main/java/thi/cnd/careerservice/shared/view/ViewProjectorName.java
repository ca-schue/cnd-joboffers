package thi.cnd.careerservice.shared.view;

import lombok.RequiredArgsConstructor;

/**
 * Unique name for the {@link ViewProjector}
 */
@RequiredArgsConstructor
public enum ViewProjectorName {

    JOB_OFFER("default-job-offer-view"),
    JOB_APPLICATION("default-job-application-view");

    private final String value;

    @Override
    public String toString() {
        return this.value;
    }

}
