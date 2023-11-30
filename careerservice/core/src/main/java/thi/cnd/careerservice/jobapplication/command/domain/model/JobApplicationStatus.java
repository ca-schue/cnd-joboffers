package thi.cnd.careerservice.jobapplication.command.domain.model;

import java.util.List;
import java.util.Optional;

public enum JobApplicationStatus {

    DRAFT,

    OPEN,

    ACCEPTED,

    DENIED,

    DELETED;

    private static List<JobApplicationStatus> entries = List.of(values());
    private static List<JobApplicationStatus> allExceptDeleted = entries.stream().filter(item -> item != DELETED).toList();

    public static Optional<JobApplicationStatus> parse(String value) {
        return entries.stream().filter(entry -> entry.name().equalsIgnoreCase(value)).findFirst();
    }

    public static List<JobApplicationStatus> allExceptDeleted() {
        return allExceptDeleted;
    }

}
