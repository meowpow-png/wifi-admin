package hr.ht.rnd.wifiadmin.test.support;

import hr.ht.rnd.wifiadmin.application.outbound.PlatformClient;
import hr.ht.rnd.wifiadmin.domain.wifi.WifiConfiguration;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Queue;

/**
 * Scripted platform client for retry integration tests.
 */
public final class RetryableTestPlatformClient implements PlatformClient {

    private final Queue<PlatformClientAction> retrieveActions = new ArrayDeque<>();
    private final Queue<PlatformClientAction> updateActions = new ArrayDeque<>();

    private int retrieveAttempts;
    private int updateAttempts;

    @Override
    public WifiConfiguration retrieveConfiguration(String cpeId) {
        retrieveAttempts++;
        return next(retrieveActions);
    }

    @Override
    public WifiConfiguration updateConfiguration(WifiConfiguration configuration) {
        updateAttempts++;
        return next(updateActions);
    }

    public void onRetrieveConfiguration(PlatformClientAction... actions) {
        Collections.addAll(retrieveActions, actions);
    }

    public void onUpdateConfiguration(PlatformClientAction... actions) {
        Collections.addAll(updateActions, actions);
    }

    public int retrieveAttempts() {
        return retrieveAttempts;
    }

    public int updateAttempts() {
        return updateAttempts;
    }

    private static WifiConfiguration next(Queue<PlatformClientAction> actions) {
        var action = actions.poll();
        if (action == null) {
            throw new IllegalStateException("No scripted platform action available");
        }
        return action.get();
    }
}
