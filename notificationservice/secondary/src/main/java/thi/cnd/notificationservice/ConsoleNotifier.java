package thi.cnd.notificationservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Component;

@Component
public class ConsoleNotifier implements EmailNotifier {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleNotifier.class);
    private static final Marker EMAIL_MARKER = MarkerFactory.getMarker("EMAIL");

    @Override
    public void sendEmailToCustomer(String email, String content) {
        logger.info(EMAIL_MARKER, "Sending email to {} with content: {}", email, content);
    }

}
