package hr.ht.rnd.wifiadmin.infra.app;

import hr.ht.rnd.wifiadmin.application.event.ApplicationEvent;
import hr.ht.rnd.wifiadmin.application.outbound.EventPublisher;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Spring-backed {@link EventPublisher} implementation.
 */
@Component
final class SpringEventPublisher implements EventPublisher {

    private final ApplicationEventPublisher publisher;

    SpringEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(ApplicationEvent event) {
        Objects.requireNonNull(event, "event must not be null");
        publisher.publishEvent(event);
    }
}
