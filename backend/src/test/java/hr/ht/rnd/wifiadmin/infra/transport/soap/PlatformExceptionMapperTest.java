package hr.ht.rnd.wifiadmin.infra.transport.soap;

import hr.ht.rnd.wifiadmin.application.exception.PlatformCommunicationException;
import hr.ht.rnd.wifiadmin.application.exception.PlatformConnectionException;
import hr.ht.rnd.wifiadmin.application.exception.PlatformTimeoutException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.http.HttpTimeoutException;
import javax.xml.stream.XMLStreamException;

import static org.assertj.core.api.Assertions.assertThat;

class PlatformExceptionMapperTest {

    @Nested
    @DisplayName("toTransportException")
    class ToTransportExceptionMethodTests {

        @Test
        @DisplayName("Maps socket timeout to timeout exception")
        void should_MapSocketTimeoutToTimeoutException_when_CauseIsSocketTimeout() {
            var cause = new SocketTimeoutException("Read timed out");
            var exception = PlatformExceptionMapper.toTransportException(cause);

            assertThat(exception).hasValueSatisfying(value -> {
                assertThat(value).isInstanceOf(PlatformTimeoutException.class);
                assertThat(value).hasCauseInstanceOf(SocketTimeoutException.class);
            });
        }

        @Test
        @DisplayName("Maps HTTP timeout to timeout exception")
        void should_MapHttpTimeoutToTimeoutException_when_CauseIsHttpTimeout() {
            var cause = new HttpTimeoutException("Request timed out");
            var exception = PlatformExceptionMapper.toTransportException(cause);

            assertThat(exception).hasValueSatisfying(value -> {
                assertThat(value).isInstanceOf(PlatformTimeoutException.class);
                assertThat(value).hasCauseInstanceOf(HttpTimeoutException.class);
            });
        }

        @Test
        @DisplayName("Maps connection failure to connection exception")
        void should_MapConnectionFailureToConnectionException_when_CauseIsConnectException() {
            var cause = new ConnectException("Connection refused");
            var exception = PlatformExceptionMapper.toTransportException(cause);

            assertThat(exception).hasValueSatisfying(value -> {
                assertThat(value).isInstanceOf(PlatformConnectionException.class);
                assertThat(value).hasCauseInstanceOf(ConnectException.class);
            });
        }

        @Test
        @DisplayName("Maps unknown host to connection exception")
        void should_MapUnknownHostToConnectionException_when_CauseIsUnknownHostException() {
            var cause = new UnknownHostException("platform.test");
            var exception = PlatformExceptionMapper.toTransportException(cause);

            assertThat(exception).hasValueSatisfying(value -> {
                assertThat(value).isInstanceOf(PlatformConnectionException.class);
                assertThat(value).hasCauseInstanceOf(UnknownHostException.class);
            });
        }

        @Test
        @DisplayName("Maps IO failure to communication exception")
        void should_MapIoFailureToCommunicationException_when_CauseIsIOException() {
            var cause = new IOException("Connection reset");
            var exception = PlatformExceptionMapper.toTransportException(cause);

            assertThat(exception).hasValueSatisfying(value -> {
                assertThat(value).isInstanceOf(PlatformCommunicationException.class);
                assertThat(value).hasCauseInstanceOf(IOException.class);
            });
        }

        @Test
        @DisplayName("Maps XML stream failure to communication exception")
        void should_MapXmlStreamFailureToCommunicationException_when_CauseIsXmlStreamException() {
            var cause = new XMLStreamException("Malformed response");
            var exception = PlatformExceptionMapper.toTransportException(cause);

            assertThat(exception).hasValueSatisfying(value -> {
                assertThat(value).isInstanceOf(PlatformCommunicationException.class);
                assertThat(value).hasCauseInstanceOf(XMLStreamException.class);
            });
        }

        @Test
        @DisplayName("Maps nested transport failure")
        void should_MapNestedTransportFailure_when_CauseChainContainsTransportFailure() {
            var cause = new SocketTimeoutException("Read timed out");
            var wrapper = new RuntimeException(new IllegalStateException(cause));

            var exception = PlatformExceptionMapper.toTransportException(wrapper);

            assertThat(exception).hasValueSatisfying(value -> {
                assertThat(value).isInstanceOf(PlatformTimeoutException.class);
                assertThat(value).hasCauseInstanceOf(SocketTimeoutException.class);
            });
        }

        @Test
        @DisplayName("Returns empty when failure cannot be mapped")
        void should_ReturnEmpty_when_FailureCannotBeMapped() {
            var cause = new IllegalStateException("Unexpected failure");
            var exception = PlatformExceptionMapper.toTransportException(cause);

            assertThat(exception).isEmpty();
        }

        @Test
        @DisplayName("Returns empty when cause chain is cyclic and cannot be mapped")
        void should_ReturnEmpty_when_CauseChainIsCyclicAndCannotBeMapped() {
            var cause = new CyclicException();
            var exception = PlatformExceptionMapper.toTransportException(cause);

            assertThat(exception).isEmpty();
        }
    }

    private static final class CyclicException extends RuntimeException {

        @Override
        public synchronized Throwable getCause() {
            return this;
        }
    }
}
