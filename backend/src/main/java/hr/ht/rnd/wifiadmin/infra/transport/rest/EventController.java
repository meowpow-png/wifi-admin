package hr.ht.rnd.wifiadmin.infra.transport.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * REST controller exposing
 * application event streams.
 */
@RestController
final class EventController {

    private final SseConfigurationChangeNotifier notifier;

    EventController(SseConfigurationChangeNotifier notifier) {
        this.notifier = notifier;
    }

    /**
     * Subscribes to application events.
     *
     * @return the event stream
     */
    @GetMapping(
            value = "/admin/events",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE
    )
    SseEmitter subscribe() {
        var emitter = new SseEmitter(0L);
        notifier.subscribe(emitter);
        return emitter;
    }
}
