package thi.cnd.careerservice.event.external.company;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import thi.cnd.careerservice.shared.event.ExternalEventListener;
import thi.cnd.careerservice.jobapplication.domain.JobApplicationCrossAggregateEventHandler;
import thi.cnd.careerservice.joboffer.domain.JobOfferExternalEventHandler;
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

    private final JobApplicationCrossAggregateEventHandler eventHandler;
    private final JobOfferExternalEventHandler jobOfferExternalEventHandler;
    private final Logger logger = LoggerFactory.getLogger(CompanyEventListener.class);

    @KafkaListener(groupId = "career-service", topics = "user-service_company-name-changed")
    public void listen(@Payload CompanyNameChangedEventDTO event) {
        logger.info("Company-Name-Changed-Event received for company {}", event.getCompanyId());
        eventHandler.updateCompanyName(new CompanyId(event.getCompanyId()), event.getName());
    }


    @KafkaListener(groupId = "career-service", topics = "user-service_company-deleted")
    public void listen(@Payload CompanyDeletedEventDTO event) {
        logger.info("Company-Deleted-Event received for company {}", event.getCompanyId());
        jobOfferExternalEventHandler.companyDeleted(new CompanyId(event.getCompanyId()));
    }

}
