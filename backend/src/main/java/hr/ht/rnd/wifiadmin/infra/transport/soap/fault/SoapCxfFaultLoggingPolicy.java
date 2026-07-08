package hr.ht.rnd.wifiadmin.infra.transport.soap.fault;

import hr.ht.rnd.wifiadmin.infra.transport.soap.cxf.CxfFaultLoggingPolicy;

import org.apache.cxf.message.Message;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

/**
 * SOAP-specific {@link CxfFaultLoggingPolicy} implementation.
 * <p>
 * <strong>Implementation Note:</strong>
 * Expected platform responses and transient transport
 * failures are excluded from CXF fault logging
 * because they are handled by the application.
 *
 * @see SoapFaultDecoder
 */
public final class SoapCxfFaultLoggingPolicy implements CxfFaultLoggingPolicy {

    @Override
    public boolean shouldLogFault(
            Exception exception,
            String description,
            Message message
    ) {
        if (hasCause(exception, ConnectException.class)) {
            return false;
        }
        if (hasCause(exception, SocketTimeoutException.class)) {
            return false;
        }
        var faultMessage = exception.getMessage();
        if (faultMessage == null) {
            return true;
        }
        return Arrays.stream(SoapFaultCode.values())
                .map(SoapFaultCode::value)
                .noneMatch(faultMessage::contains);
    }

    private static boolean hasCause(
            Throwable throwable,
            Class<? extends Throwable> type
    ) {
        while (throwable != null) {
            if (type.isInstance(throwable)) {
                return true;
            }
            throwable = throwable.getCause();
        }
        return false;
    }
}
