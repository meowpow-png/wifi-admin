package hr.ht.rnd.wifiadmin.infra.transport.soap.cxf;

import org.apache.cxf.logging.FaultListener;
import org.apache.cxf.message.Message;

import java.util.Objects;

/**
 * Controls logging of SOAP faults reported by Apache CXF.
 */
final class CxfFaultListener implements FaultListener {

    private final boolean logFaults;
    private final CxfFaultLoggingPolicy policy;

    CxfFaultListener(boolean logFaults, CxfFaultLoggingPolicy policy) {
        Objects.requireNonNull(policy, "policy must not be null");

        this.logFaults = logFaults;
        this.policy = policy;
    }

    @Override
    public boolean faultOccurred(
            Exception exception,
            String description,
            Message message
    ) {
        return logFaults && policy.shouldLogFault(
                exception,
                description,
                message
        );
    }
}
