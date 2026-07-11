package hr.ht.rnd.wifiadmin.infra.transport.soap;

import hr.ht.rnd.wifiadmin.application.exception.PlatformConnectionException;
import hr.ht.rnd.wifiadmin.application.outbound.PlatformClient;
import hr.ht.rnd.wifiadmin.domain.wifi.WifiConfiguration;
import hr.ht.rnd.wifiadmin.infra.transport.client.ResilientClient;
import hr.ht.rnd.wifiadmin.infra.transport.soap.retry.SoapRequestContext;
import hr.ht.rnd.wifiadmin.infra.transport.soap.retry.SoapRetryOperation;

import org.springframework.core.retry.RetryTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static hr.ht.rnd.wifiadmin.common.StructuredLog.*;

/**
 * {@link PlatformClient} implementation that retries
 * transient failures when communicating with the SOAP platform.
 */
final class ResilientSoapPlatformClient
        extends ResilientClient<SoapRequestContext>
        implements PlatformClient {

    private static final Logger log = LoggerFactory.getLogger(ResilientSoapPlatformClient.class);

    private final PlatformClient delegate;

    /**
     * Creates a new resilient SOAP client.
     *
     * @param retryTemplate the retry template
     * @param delegate the SOAP client to decorate
     */
    ResilientSoapPlatformClient(RetryTemplate retryTemplate, PlatformClient delegate) {
        super(retryTemplate);

        this.delegate = delegate;
    }

    @Override
    public WifiConfiguration retrieveConfiguration(String cpeId) {
        Objects.requireNonNull(cpeId, "cpeId must not be null");

        var context = new SoapRequestContext(
                "GetCpeID",
                cpeId
        );
        var operation = new SoapRetryOperation<>(
                () -> delegate.retrieveConfiguration(cpeId),
                context
        );
        return executeWithRetry(operation, context);
    }

    @Override
    public WifiConfiguration updateConfiguration(WifiConfiguration configuration) {
        Objects.requireNonNull(configuration, "configuration must not be null");

        var context = new SoapRequestContext(
                "UpdateCpeId",
                configuration.cpeId()
        );
        var operation = new SoapRetryOperation<>(
                () -> delegate.updateConfiguration(configuration),
                context
        );
        return executeWithRetry(operation, context);
    }

    @Override
    protected <T> T recoverOrThrow(Throwable cause, SoapRequestContext context) {
        if (cause instanceof RuntimeException ex) {
            throw ex;
        }
        var message = "Platform communication failed for %s";
        throw new PlatformConnectionException(
                message.formatted(context),
                cause
        );
    }

    @Override
    protected void onRecovery(Throwable cause, SoapRequestContext context) {
        warn(log).withEvent(Event.PLATFORM_RETRY_EXHAUSTED)
                .withField(Field.PLATFORM_REQUEST_CONTEXT, context.operation())
                .withField(Field.CPE_ID, context.cpeId())
                .withCause(cause)
                .log();
    }
}
