package hr.ht.rnd.wifiadmin.infra.transport.soap;

import hr.ht.rnd.wifiadmin.application.exception.PlatformCommunicationException;
import hr.ht.rnd.wifiadmin.application.exception.PlatformConnectionException;
import hr.ht.rnd.wifiadmin.application.exception.PlatformTimeoutException;
import hr.ht.rnd.wifiadmin.application.exception.PlatformTransportException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.http.HttpTimeoutException;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Optional;
import java.util.Set;
import javax.xml.stream.XMLStreamException;

/**
 * Maps SOAP client exceptions to platform exceptions.
 */
public final class PlatformExceptionMapper {

    private PlatformExceptionMapper() {}

    /**
     * Maps a SOAP client failure to the corresponding
     * platform transport exception by inspecting its cause chain.
     * <p>
     * <strong>Implementation Note:</strong>
     * Cause chains are traversed defensively to prevent
     * infinite recursion when processing malformed exceptions.
     *
     * @param cause the failure to map
     *
     * @return the mapped transport exception, if any
     */
    public static Optional<PlatformTransportException> toTransportException(Throwable cause) {
        return toTransportException(cause, Collections.newSetFromMap(new IdentityHashMap<>()));
    }

    private static Optional<PlatformTransportException> toTransportException(
            Throwable throwable,
            Set<Throwable> visited
    ) {
        if (!visited.add(throwable)) {
            return Optional.empty();
        }
        return classify(throwable).or(() ->
                toTransportException(throwable.getCause(), visited)
        );
    }

    private static Optional<PlatformTransportException> classify(Throwable throwable) {
        if (isAnyOf(throwable, SocketTimeoutException.class, HttpTimeoutException.class)) {
            var message = "Communication with the platform timed out";
            return Optional.of(new PlatformTimeoutException(message, throwable));
        }
        if (isAnyOf(throwable, ConnectException.class, UnknownHostException.class)) {
            var message = "Platform could not be reached";
            return Optional.of(new PlatformConnectionException(message, throwable));
        }
        if (isAnyOf(throwable, IOException.class, XMLStreamException.class)) {
            var message = "Communication with the platform failed";
            return Optional.of(new PlatformCommunicationException(message, throwable));
        }
        return Optional.empty();
    }

    @SafeVarargs
    private static boolean isAnyOf(
            Throwable throwable,
            Class<? extends Throwable>... types
    ) {
        for (var type : types) {
            if (type.isInstance(throwable)) {
                return true;
            }
        }
        return false;
    }
}
