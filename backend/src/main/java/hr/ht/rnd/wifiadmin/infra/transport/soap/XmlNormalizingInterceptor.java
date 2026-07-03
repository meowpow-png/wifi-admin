package hr.ht.rnd.wifiadmin.infra.transport.soap;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Normalizes inbound SOAP responses to improve
 * interoperability with platform implementations
 * that produce non-standard XML documents.
 * <p>
 * <strong>Implementation Note:</strong>
 * Some SOAP platforms emit leading whitespace before
 * the XML declaration, which violates the XML specification
 * and causes strict XML parsers to reject otherwise valid
 * SOAP messages. This interceptor removes only the invalid
 * leading whitespace before XML parsing while leaving
 * the SOAP document itself unchanged.
 */
final class XmlNormalizingInterceptor extends AbstractPhaseInterceptor<Message> {

    private static final byte[] XML_PREFIX = "<?xml".getBytes(StandardCharsets.US_ASCII);

    XmlNormalizingInterceptor() {
        super(Phase.RECEIVE);
    }

    /**
     * <strong>Implementation Note:</strong>
     * The inbound stream is owned by Apache
     * CXF and is intentionally not closed here,
     * as its lifecycle is managed by the framework.
     */
    @Override
    public void handleMessage(Message message) throws Fault {
        var in = message.getContent(InputStream.class);
        try {
            byte[] bytes = IOUtils.readBytesFromStream(in);
            int offset = findXmlDeclaration(bytes);

            if (offset > 0) {
                bytes = Arrays.copyOfRange(bytes, offset, bytes.length);
            }
            message.setContent(InputStream.class, new ByteArrayInputStream(bytes));
        }
        catch (IOException ex) {
            throw new Fault(ex);
        }
    }

    private static int findXmlDeclaration(byte[] bytes) {
        int offset = 0;

        while (offset < bytes.length) {
            byte b = bytes[offset];
            if (b == ' ' || b == '\t' || b == '\r' || b == '\n') {
                offset++;
            }
            else {
                break;
            }
        }
        if (offset == 0) {
            return 0;
        }
        if (offset + XML_PREFIX.length > bytes.length) {
            return -1;
        }
        for (int i = 0; i < XML_PREFIX.length; i++) {
            if (bytes[offset + i] != XML_PREFIX[i]) {
                return -1;
            }
        }
        return offset;
    }
}
