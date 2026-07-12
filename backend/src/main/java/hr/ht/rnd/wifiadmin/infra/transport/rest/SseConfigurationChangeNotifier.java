package hr.ht.rnd.wifiadmin.infra.transport.rest;

import hr.ht.rnd.wifiadmin.application.outbound.ConfigurationChangeNotifier;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Spring-backed {@link ConfigurationChangeNotifier}
 * implementation using Server-Sent Events.
 */
@Component
final class SseConfigurationChangeNotifier implements ConfigurationChangeNotifier {

    private final Set<SseEmitter> emitters = ConcurrentHashMap.newKeySet();

    void subscribe(SseEmitter emitter) {
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(ex -> emitters.remove(emitter));
    }

    @Override
    public void notifyConfigurationsChanged() {
        var event = SseEmitter.event().name(
                "configurations-changed"
        );
        emitters.removeIf(emitter -> {
            try {
                emitter.send(event);
                return false;
            }
            catch (IOException e) {
                return true;
            }
        });
    }
}
