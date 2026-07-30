package hr.ht.rnd.wifiadmin.infra.transport.soap.logging;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.io.CacheAndWriteOutputStream;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.io.CachedOutputStreamCallback;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

/**
 * Logs outbound SOAP requests sent to the platform.
 * <p>
 * <strong>Implementation Note:</strong>
 * The request is logged after JAXB marshalling and
 * before the SOAP message is written to the transport.
 */
public final class SoapRequestLoggingInterceptor extends AbstractPhaseInterceptor<Message> {

    public SoapRequestLoggingInterceptor() {
        super(Phase.PRE_STREAM);
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        if (SoapPayloadLogger.isDisabled()) {
            return;
        }
        var originalStream = message.getContent(OutputStream.class);
        var cachedStream = new CacheAndWriteOutputStream(originalStream);
        cachedStream.registerCallback(new LoggingCallback(message, originalStream));

        message.setContent(OutputStream.class, cachedStream);
    }

    @SuppressWarnings("ClassCanBeRecord")
    private static final class LoggingCallback implements CachedOutputStreamCallback {

        private final Message message;
        private final OutputStream originalStream;

        private LoggingCallback(Message message, OutputStream originalStream) {
            this.message = message;
            this.originalStream = originalStream;
        }

        @Override
        public void onFlush(CachedOutputStream stream) {}

        @Override
        public void onClose(CachedOutputStream stream) {
            try {
                var payload = IOUtils.toString(
                        stream.getInputStream(),
                        StandardCharsets.UTF_8.name()
                );
                SoapPayloadLogger.logRequest(payload);

                stream.lockOutputStream();
                stream.resetOut(null, false);
            }
            catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            finally {
                message.setContent(OutputStream.class, originalStream);
            }
        }
    }
}
