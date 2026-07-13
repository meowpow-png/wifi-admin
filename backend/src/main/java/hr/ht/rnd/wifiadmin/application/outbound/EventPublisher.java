package hr.ht.rnd.wifiadmin.application.outbound;

import hr.ht.rnd.wifiadmin.application.event.ApplicationEvent;

/**
 * Publishes application events.
 */
public interface EventPublisher {

    /**
     * Publishes the specified application event.
     *
     * @param event the application event to publish
     *
     * @throws NullPointerException if {@code event} is {@code null}
     */
    void publish(ApplicationEvent event);
}
