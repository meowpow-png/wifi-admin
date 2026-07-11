package hr.ht.rnd.wifiadmin.infra.transport.client;

import org.springframework.core.retry.RetryTemplate;
import org.springframework.core.retry.Retryable;

import org.jspecify.annotations.NullMarked;

@SuppressWarnings("SameParameterValue")
final class TestResilientClient extends ResilientClient<String> {

    private Object recoveredResult;
    private RuntimeException recoveryFailure;
    private Throwable recoveryCause;
    private String recoveryContext;
    private boolean onRecoveryCalled;
    private Throwable onRecoveryCause;
    private String onRecoveryContext;

    TestResilientClient(RetryTemplate retryTemplate) {
        super(retryTemplate);
    }

    <T> T execute(Retryable<T> action, String context) {
        return executeWithRetry(action, context);
    }

    void recoverWith(Object recoveredResult) {
        this.recoveredResult = recoveredResult;
    }

    void failRecoveryWith(RuntimeException recoveryFailure) {
        this.recoveryFailure = recoveryFailure;
    }

    @Override
    @NullMarked
    @SuppressWarnings("unchecked")
    protected <T> T recoverOrThrow(Throwable cause, String context) {
        recoveryCause = cause;
        recoveryContext = context;

        if (recoveryFailure != null) {
            throw recoveryFailure;
        }
        return (T) recoveredResult;
    }

    @Override
    @NullMarked
    protected void onRecovery(Throwable cause, String context) {
        onRecoveryCalled = true;
        onRecoveryCause = cause;
        onRecoveryContext = context;
    }

    Throwable recoveryCause() {
        return recoveryCause;
    }

    String recoveryContext() {
        return recoveryContext;
    }

    boolean onRecoveryCalled() {
        return onRecoveryCalled;
    }

    Throwable onRecoveryCause() {
        return onRecoveryCause;
    }

    String onRecoveryContext() {
        return onRecoveryContext;
    }
}
