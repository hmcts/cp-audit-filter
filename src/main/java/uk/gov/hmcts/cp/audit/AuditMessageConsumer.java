package uk.gov.hmcts.cp.audit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ConditionalOnProperty(name = "audit.http.enabled", havingValue = "true")
public class AuditMessageConsumer {
    @JmsListener(destination = "jms.topic.auditing.event")
    public void on(final String message) {
        log.info("Audit payload: {}", message);
    }
}
