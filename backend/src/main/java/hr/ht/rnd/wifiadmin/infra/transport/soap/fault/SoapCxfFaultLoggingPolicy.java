package hr.ht.rnd.wifiadmin.infra.transport.soap.fault;

import hr.ht.rnd.wifiadmin.infra.transport.soap.cxf.CxfFaultLoggingPolicy;

import org.apache.cxf.message.Message;

import java.util.Arrays;

/**
 * SOAP-specific {@link CxfFaultLoggingPolicy} implementation.
 * <p>
 * <strong>Implementation Note:</strong>
 * All known SOAP fault codes are excluded from
 * CXF fault logging because they are expected
 * platform responses rather than application failures.
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
        var faultMessage = exception.getMessage();
        if (faultMessage == null) {
            return true;
        }
        return Arrays.stream(SoapFaultCode.values())
                .map(SoapFaultCode::value)
                .noneMatch(faultMessage::contains);
    }
}
