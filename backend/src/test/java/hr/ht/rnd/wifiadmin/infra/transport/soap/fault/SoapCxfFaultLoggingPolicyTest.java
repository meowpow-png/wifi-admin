package hr.ht.rnd.wifiadmin.infra.transport.soap.fault;

import jakarta.xml.ws.WebServiceException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.net.SocketTimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

class SoapCxfFaultLoggingPolicyTest {

    @Nested
    @SuppressWarnings("DataFlowIssue")
    @DisplayName("shouldLogFault")
    class ShouldLogFaultMethodTests {

        @Test
        @DisplayName("Returns false when exception is a transport failure")
        void should_ReturnFalse_when_ExceptionIsTransportFailure() {
            var exception = new WebServiceException(new SocketTimeoutException("Read timed out"));
            var shouldLogFault = faultLoggingPolicy().shouldLogFault(
                    exception,
                    null,
                    null
            );
            assertThat(shouldLogFault).isFalse();
        }

        @Test
        @DisplayName("Returns false when exception contains a recognized SOAP fault")
        void should_ReturnFalse_when_ExceptionContainsRecognizedSoapFault() {
            var shouldLogFault = faultLoggingPolicy().shouldLogFault(
                    TestWebServiceExceptions.notFound(),
                    null,
                    null
            );
            assertThat(shouldLogFault).isFalse();
        }

        @Test
        @DisplayName("Returns true when exception is not handled by application")
        void should_ReturnTrue_when_ExceptionIsNotHandledByApplication() {
            var shouldLogFault = faultLoggingPolicy().shouldLogFault(
                    TestWebServiceExceptions.unknown(),
                    null,
                    null
            );
            assertThat(shouldLogFault).isTrue();
        }
    }

    private static SoapCxfFaultLoggingPolicy faultLoggingPolicy() {
        return new SoapCxfFaultLoggingPolicy();
    }
}
