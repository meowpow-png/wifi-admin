package hr.ht.rnd.wifiadmin.infra.transport.soap.retry;

import org.springframework.core.retry.RetryListener;
import org.springframework.core.retry.RetryPolicy;
import org.springframework.core.retry.RetryState;
import org.springframework.core.retry.Retryable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static hr.ht.rnd.wifiadmin.common.StructuredLog.*;

/**
 * Logs retry attempts for outbound SOAP operations.
 */
public final class SoapRetryLoggingListener implements RetryListener {

    private static final Logger log = LoggerFactory.getLogger(SoapRetryLoggingListener.class);

    @Override
    public void beforeRetry(
            RetryPolicy retryPolicy,
            Retryable<?> retryable,
            RetryState retryState
    ) {
        warn(log).withEvent(Event.PLATFORM_RETRY_ATTEMPT)
                .withField(Field.PLATFORM_REQUEST_CONTEXT, retryable.getName())
                .withField(Field.RETRY_COUNT, retryState.getRetryCount())
                .withCause(retryState.getLastException())
                .log();
    }
}
