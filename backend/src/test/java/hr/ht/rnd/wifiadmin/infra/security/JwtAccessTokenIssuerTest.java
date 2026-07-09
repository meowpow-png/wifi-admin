package hr.ht.rnd.wifiadmin.infra.security;

import hr.ht.rnd.wifiadmin.domain.account.TestAccounts;
import hr.ht.rnd.wifiadmin.infra.app.TestClock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtAccessTokenIssuerTest {

    @Nested
    @DisplayName("issue")
    class IssueMethodTests {

        @Test
        @DisplayName("Returns signed token when username is valid")
        void should_ReturnSignedToken_when_UsernameIsValid() {
            var clock = TestClock.create();
            var properties = TestSecurityProperties.builder().build();
            var issuer = TestJwts.tokenIssuer(properties, clock);

            var secret = properties.jwtSecret();
            var username = TestAccounts.admin().username();
            var token = issuer.issue(username);
            var claims = TestJwts.parser(clock, secret)
                    .parseSignedClaims(token)
                    .getPayload();

            assertThat(claims.getSubject()).isEqualTo(username);
        }

        @Test
        @DisplayName("Sets validity window when token is issued")
        void should_SetValidityWindow_when_TokenIsIssued() {
            var clock = TestClock.create();
            var now = clock.instant();
            var expiration = Duration.ofMinutes(30);
            var properties = TestSecurityProperties.builder()
                    .withJwtExpiration(expiration)
                    .build();

            var issuer = TestJwts.tokenIssuer(properties, clock);
            var secret = properties.jwtSecret();
            var token = issuer.issue(TestAccounts.admin().username());
            var claims = TestJwts.parser(clock, secret)
                    .parseSignedClaims(token)
                    .getPayload();

            assertThat(claims.getIssuedAt()).isEqualTo(Date.from(now));
            assertThat(claims.getExpiration()).isEqualTo(Date.from(now.plus(expiration)));
        }

        @Test
        @SuppressWarnings("DataFlowIssue")
        @DisplayName("Throws NullPointerException when username is null")
        void should_ThrowNullPointerException_when_UsernameIsNull() {
            assertThatThrownBy(() -> TestJwts.tokenIssuer().issue(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Throws IllegalArgumentException when username is blank")
        void should_ThrowIllegalArgumentException_when_UsernameIsBlank() {
            assertThatThrownBy(() -> TestJwts.tokenIssuer().issue(" "))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
