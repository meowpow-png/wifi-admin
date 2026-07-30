package hr.ht.rnd.wifiadmin.infra.transport.soap.cxf;

import org.apache.cxf.message.Message;

/**
 * Policy controlling which Apache
 * CXF faults should be logged.
 */
public interface CxfFaultLoggingPolicy {

    /**
     * Returns whether the specified fault should be logged.
     *
     * @param exception the exception representing the fault
     * @param description the fault description
     * @param message the associated CXF message
     */
    boolean shouldLogFault(
            Exception exception,
            String description,
            Message message
    );
}
