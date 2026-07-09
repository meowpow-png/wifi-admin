package hr.ht.rnd.wifiadmin.infra.transport.soap;

import java.time.Duration;
import java.time.LocalTime;

public final class TestPlatformProperties {

    private TestPlatformProperties() {}

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String soapEndpoint = "https://platform.example.com/soap";
        private String cpeIdFormat = "CPE-%05d";
        private int cpeIdCount = 10;
        private boolean syncOnStartup = true;
        private LocalTime syncSchedule = LocalTime.of(2, 0);
        private Duration connectionTimeout = Duration.ofSeconds(5);
        private Duration receiveTimeout = Duration.ofSeconds(30);

        private int retryMaxAttempts = 3;
        private Duration retryDelay = Duration.ofMillis(250);
        private Duration retryMaxDelay = Duration.ofSeconds(5);
        private double retryDelayMultiplier = 2.0;

        private Builder() {}

        public Builder withSoapEndpoint(String soapEndpoint) {
            this.soapEndpoint = soapEndpoint;
            return this;
        }

        public Builder withCpeIdFormat(String cpeIdFormat) {
            this.cpeIdFormat = cpeIdFormat;
            return this;
        }

        public Builder withCpeIdCount(int cpeIdCount) {
            this.cpeIdCount = cpeIdCount;
            return this;
        }

        public Builder withSyncOnStartup(boolean syncOnStartup) {
            this.syncOnStartup = syncOnStartup;
            return this;
        }

        public Builder withSyncSchedule(LocalTime syncSchedule) {
            this.syncSchedule = syncSchedule;
            return this;
        }

        public Builder withConnectionTimeout(Duration connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public Builder withReceiveTimeout(Duration receiveTimeout) {
            this.receiveTimeout = receiveTimeout;
            return this;
        }

        public Builder withRetryMaxAttempts(int retryMaxAttempts) {
            this.retryMaxAttempts = retryMaxAttempts;
            return this;
        }

        public Builder withRetryDelay(Duration retryDelay) {
            this.retryDelay = retryDelay;
            return this;
        }

        public Builder withRetryMaxDelay(Duration retryMaxDelay) {
            this.retryMaxDelay = retryMaxDelay;
            return this;
        }

        public Builder withRetryDelayMultiplier(double retryDelayMultiplier) {
            this.retryDelayMultiplier = retryDelayMultiplier;
            return this;
        }

        public Builder withRetry(PlatformProperties.Retry retry) {
            this.retryMaxAttempts = retry.maxAttempts();
            this.retryDelay = retry.delay();
            this.retryMaxDelay = retry.maxDelay();
            this.retryDelayMultiplier = retry.delayMultiplier();
            return this;
        }

        public PlatformProperties build() {
            return new PlatformProperties(
                    soapEndpoint,
                    cpeIdFormat,
                    cpeIdCount,
                    syncOnStartup,
                    syncSchedule,
                    connectionTimeout,
                    receiveTimeout,
                    new PlatformProperties.Retry(
                            retryMaxAttempts,
                            retryDelay,
                            retryMaxDelay,
                            retryDelayMultiplier
                    )
            );
        }
    }
}
