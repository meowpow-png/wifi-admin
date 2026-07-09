package hr.ht.rnd.wifiadmin.infra.security;

import java.time.Duration;
import java.util.List;

public final class TestSecurityProperties {

    private TestSecurityProperties() {}

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String aesKey = "MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=";
        private String jwtSecret = "MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=";
        private Duration jwtExpiration = Duration.ofMinutes(15);
        private List<String> allowedOrigins = List.of("https://admin.example.com");
        private List<String> publicEndpoints = List.of("/auth/login");

        private Builder() {}

        public Builder withAesKey(String aesKey) {
            this.aesKey = aesKey;
            return this;
        }

        public Builder withJwtSecret(String jwtSecret) {
            this.jwtSecret = jwtSecret;
            return this;
        }

        public Builder withJwtExpiration(Duration jwtExpiration) {
            this.jwtExpiration = jwtExpiration;
            return this;
        }

        public Builder withAllowedOrigins(List<String> allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
            return this;
        }

        public Builder withPublicEndpoints(List<String> publicEndpoints) {
            this.publicEndpoints = publicEndpoints;
            return this;
        }

        public SecurityProperties build() {
            return new SecurityProperties(
                    aesKey,
                    jwtSecret,
                    jwtExpiration,
                    allowedOrigins,
                    publicEndpoints
            );
        }
    }
}
