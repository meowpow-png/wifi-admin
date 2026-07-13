package hr.ht.rnd.wifiadmin.infra.platform;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
public final class XmlNormalizingInterceptor extends AbstractPhaseInterceptor<Message> {

    public XmlNormalizingInterceptor() {
        super(Phase.RECEIVE);
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        InputStream in = message.getContent(InputStream.class);

        if (in == null) {
            return;
        }
        try {
            byte[] bytes = IOUtils.readBytesFromStream(in);
            int offset = 0;

            while (offset < bytes.length) {
                byte b = bytes[offset];
                if (b == ' ' || b == '\t' || b == '\r' || b == '\n') {
                    offset++;
                    continue;
                }
                break;
            }
            if (offset > 0
                    && offset + 5 <= bytes.length
                    && bytes[offset] == '<'
                    && bytes[offset + 1] == '?'
                    && bytes[offset + 2] == 'x'
                    && bytes[offset + 3] == 'm'
                    && bytes[offset + 4] == 'l') {

                bytes = Arrays.copyOfRange(bytes, offset, bytes.length);
            }
            message.setContent(
                    InputStream.class,
                    new ByteArrayInputStream(bytes)
            );
        }
        catch (IOException ex) {
            throw new Fault(ex);
        }
    }
}
