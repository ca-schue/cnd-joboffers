package thi.cnd.careerservice.event.external.company.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import thi.cnd.careerservice.jobapplication.command.port.JobApplicationExternalEventHandler;
import thi.cnd.careerservice.shared.event.ExternalEventListener;
import thi.cnd.careerservice.joboffer.command.port.JobOfferExternalEventHandler;
import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.event.userservice.generated.model.CompanyDeletedEventDTO;
import thi.cnd.careerservice.event.userservice.generated.model.CompanyNameChangedEventDTO;
import lombok.RequiredArgsConstructor;

/**
 * Listens to external events from the user-service, which concerns a company
 */
@Component
@RequiredArgsConstructor
public class CompanyEventListener implements ExternalEventListener {

    private final JobApplicationExternalEventHandler jobApplicationExternalEventHandler;
    private final JobOfferExternalEventHandler jobOfferExternalEventHandler;
    private final Logger logger = LoggerFactory.getLogger(CompanyEventListener.class);

    @KafkaListener(groupId = "career-service", topics = "user-service_company-name-changed")
    public void listen(@Payload CompanyNameChangedEventDTO event) {
        logger.info("Company-Name-Changed-Event received for company {}", event.getCompanyId());
        jobApplicationExternalEventHandler.updateCompanyName(new CompanyId(event.getCompanyId()), event.getName());
    }


    @KafkaListener(groupId = "career-service", topics = "user-service_company-deleted")
    public void listen(@Payload CompanyDeletedEventDTO event) {
        logger.info("Company-Deleted-Event received for company {}", event.getCompanyId());
        jobOfferExternalEventHandler.companyDeleted(new CompanyId(event.getCompanyId()));
    }

}
