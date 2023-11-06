package thi.cnd.careerservice.command.jobapplication;

import org.junit.jupiter.api.Test;

import thi.cnd.careerservice.AbstractIntegrationTest;
import thi.cnd.careerservice.MockQueryPorts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MockQueryPorts
class BasicJobApplicationCommandIT extends AbstractIntegrationTest {

    @Test
    void basicUsageTest() {
        var jobApplicationWithVersion = jobApplicationFixture.createDraft().build();

        var events = getAllEvents(jobApplicationWithVersion.get().getId());
        assertEquals(1, events.size());
    }


}
