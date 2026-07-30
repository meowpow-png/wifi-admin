package hr.ht.rnd.wifiadmin.infra.transport.soap;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.MessageImpl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class XmlNormalizingInterceptorTest {

    private final XmlNormalizingInterceptor interceptor = new XmlNormalizingInterceptor();

    @Nested
    @DisplayName("handleMessage")
    class HandleMessageMethodTests {

        @Test
        @DisplayName("Removes leading whitespace before XML declaration")
        void should_RemoveLeadingWhitespace_when_XmlDeclarationHasLeadingWhitespace() throws IOException {
            var message = message("\n\t <?xml version=\"1.0\"?><root/>");

            interceptor.handleMessage(message);

            assertThat(content(message)).isEqualTo("<?xml version=\"1.0\"?><root/>");
        }

        @Test
        @DisplayName("Leaves XML unchanged when XML declaration has no leading whitespace")
        void should_LeaveXmlUnchanged_when_XmlDeclarationHasNoLeadingWhitespace() throws IOException {
            var xml = "<?xml version=\"1.0\"?><root/>";
            var message = message(xml);

            interceptor.handleMessage(message);

            assertThat(content(message)).isEqualTo(xml);
        }

        @Test
        @DisplayName("Leaves XML unchanged when leading whitespace is not followed by XML declaration")
        void should_LeaveXmlUnchanged_when_LeadingWhitespaceIsNotFollowedByXmlDeclaration()
                throws IOException {
            var xml = "\n\t <root/>";
            var message = message(xml);

            interceptor.handleMessage(message);

            assertThat(content(message)).isEqualTo(xml);
        }

        @Test
        @DisplayName("Throws Fault when input stream cannot be read")
        void should_ThrowFault_when_InputStreamCannotBeRead() {
            var message = new MessageImpl();
            message.setContent(InputStream.class, new FailingInputStream());

            assertThatThrownBy(() -> interceptor.handleMessage(message))
                    .isInstanceOf(Fault.class)
                    .hasCauseInstanceOf(IOException.class);
        }
    }

    private static MessageImpl message(String content) {
        var message = new MessageImpl();
        message.setContent(
                InputStream.class,
                new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8))
        );
        return message;
    }

    private static String content(MessageImpl message) throws IOException {
        return new String(
                message.getContent(InputStream.class).readAllBytes(),
                StandardCharsets.UTF_8
        );
    }

    private static final class FailingInputStream extends InputStream {

        @Override
        public int read() throws IOException {
            throw new IOException("read failed");
        }
    }
}
