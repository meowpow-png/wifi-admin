package hr.ht.rnd.wifiadmin.infra.transport.soap.fault;

import hr.ht.rnd.wifiadmin.infra.transport.soap.PlatformExceptionMapper;
import hr.ht.rnd.wifiadmin.infra.transport.soap.cxf.CxfFaultLoggingPolicy;

import org.apache.cxf.message.Message;

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
        if (isTransportFailure(exception)) {
            return false;
        }
        return !SoapFaultDecoder.isSoapFault(exception);
    }

    private static boolean isTransportFailure(Throwable t) {
        while (t != null) {
            if (PlatformExceptionMapper.toTransportException(t).isPresent()) {
                return true;
            }
            t = t.getCause();
        }
        return false;
    }
}
