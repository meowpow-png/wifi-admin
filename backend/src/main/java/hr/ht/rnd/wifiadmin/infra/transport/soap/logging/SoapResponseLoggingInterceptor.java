package hr.ht.rnd.wifiadmin.infra.transport.soap.logging;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Logs inbound SOAP responses received from the platform.
 * <p>
 * <strong>Implementation Note:</strong>
 * The response is logged before SOAP processing and JAXB
 * unmarshalling so that the original payload is preserved.
 */
public final class SoapResponseLoggingInterceptor extends AbstractPhaseInterceptor<Message> {

    private static final Charset PAYLOAD_CHARSET = StandardCharsets.UTF_8;

    public SoapResponseLoggingInterceptor() {
        super(Phase.RECEIVE);
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        if (SoapPayloadLogger.isDisabled()) {
            return;
        }
        var inputStream = message.getContent(InputStream.class);
        var payload = parsePayload(inputStream);

        SoapPayloadLogger.logResponse(payload);

        message.setContent(InputStream.class, payloadToStream(payload));
    }

    private static String parsePayload(InputStream inputStream) {
        try {
            return IOUtils.toString(inputStream, PAYLOAD_CHARSET.name());
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static ByteArrayInputStream payloadToStream(String payload) {
        var bytes = payload.getBytes(PAYLOAD_CHARSET);
        return new ByteArrayInputStream(bytes);
    }
}

