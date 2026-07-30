package hr.ht.rnd.wifiadmin.infra.transport.soap.fault;

import jakarta.xml.ws.WebServiceException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SoapFaultDecoderTest {

    @Nested
    @DisplayName("decode")
    class DecodeMethodTests {

        @Test
        @DisplayName("Decodes not found fault")
        void should_DecodeNotFoundFault_when_ExceptionContainsNotFoundFaultCode() {
            var exception = TestWebServiceExceptions.notFound();
            var fault = SoapFaultDecoder.decode(exception);

            assertThat(fault.code()).isEqualTo(SoapFaultCode.NOT_FOUND);
            assertThat(fault).hasCauseInstanceOf(WebServiceException.class);
        }

        @Test
        @DisplayName("Decodes client fault")
        void should_DecodeClientFault_when_ExceptionContainsClientFaultCode() {
            var exception = TestWebServiceExceptions.client();
            var fault = SoapFaultDecoder.decode(exception);

            assertThat(fault.code()).isEqualTo(SoapFaultCode.CLIENT);
            assertThat(fault).hasCauseInstanceOf(WebServiceException.class);
        }

        @Test
        @SuppressWarnings("ThrowableNotThrown")
        @DisplayName("Throws IllegalArgumentException when exception does not contain a recognized fault")
        void should_ThrowIllegalArgumentException_when_ExceptionDoesNotContainRecognizedFault() {
            var exception = TestWebServiceExceptions.unknown();

            assertThatThrownBy(() -> SoapFaultDecoder.decode(exception))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasCauseInstanceOf(WebServiceException.class);
        }

        @Test
        @SuppressWarnings("ThrowableNotThrown")
        @DisplayName("Throws IllegalArgumentException when exception message is null")
        void should_ThrowIllegalArgumentException_when_ExceptionMessageIsNull() {
            var exception = TestWebServiceExceptions.nullMessage();

            assertThatThrownBy(() -> SoapFaultDecoder.decode(exception))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasCauseInstanceOf(WebServiceException.class);
        }
    }

    @Nested
    @DisplayName("isSoapFault")
    class IsSoapFaultMethodTests {

        @Test
        @DisplayName("Returns true when exception contains a recognized fault")
        void should_ReturnTrue_when_ExceptionContainsRecognizedFault() {
            var exception = TestWebServiceExceptions.notFound();

            assertThat(SoapFaultDecoder.isSoapFault(exception)).isTrue();
        }

        @Test
        @DisplayName("Returns false when exception does not contain a recognized fault")
        void should_ReturnFalse_when_ExceptionDoesNotContainRecognizedFault() {
            var exception = TestWebServiceExceptions.unknown();

            assertThat(SoapFaultDecoder.isSoapFault(exception)).isFalse();
        }

        @Test
        @DisplayName("Returns false when exception message is null")
        void should_ReturnFalse_when_ExceptionMessageIsNull() {
            var exception = TestWebServiceExceptions.nullMessage();

            assertThat(SoapFaultDecoder.isSoapFault(exception)).isFalse();
        }
    }
}
