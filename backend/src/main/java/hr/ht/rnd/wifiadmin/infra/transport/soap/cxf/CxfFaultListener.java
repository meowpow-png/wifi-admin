package hr.ht.rnd.wifiadmin.infra.transport.soap.cxf;

import org.apache.cxf.logging.FaultListener;
import org.apache.cxf.message.Message;

import java.util.function.BooleanSupplier;

/**
 * Controls logging of SOAP faults reported by Apache CXF.
 */
final class CxfFaultListener implements FaultListener {

    private final BooleanSupplier logSoapFaults;

    CxfFaultListener(BooleanSupplier logSoapFaults) {
        this.logSoapFaults = logSoapFaults;
    }

    @Override
    public boolean faultOccurred(
            Exception exception,
            String description,
            Message message
    ) {
        return logSoapFaults.getAsBoolean();
    }
}
