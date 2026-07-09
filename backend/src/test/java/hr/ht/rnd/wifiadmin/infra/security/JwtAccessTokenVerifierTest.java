package hr.ht.rnd.wifiadmin.infra.security;

import hr.ht.rnd.wifiadmin.application.exception.AuthenticationException;
import hr.ht.rnd.wifiadmin.domain.account.TestAccounts;
import hr.ht.rnd.wifiadmin.infra.app.TestClock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtAccessTokenVerifierTest {

    @Nested
    @DisplayName("verify")
    class VerifyMethodTests {

        @Test
        @DisplayName("Returns username when token is valid")
        void should_ReturnUsername_when_TokenIsValid() {
            var clock = TestClock.create();
            var properties = TestSecurityProperties.builder().build();
            var issuer = TestJwts.tokenIssuer(properties, clock);
            var verifier = TestJwts.tokenVerifier(properties, clock);
            var expectedUsername = TestAccounts.admin().username();
            var token = issuer.issue(expectedUsername);

            var username = verifier.verify(token);

            assertThat(username).isEqualTo(expectedUsername);
        }

        @Test
        @SuppressWarnings("DataFlowIssue")
        @DisplayName("Throws NullPointerException when token is null")
        void should_ThrowNullPointerException_when_TokenIsNull() {
            assertThatThrownBy(() -> TestJwts.tokenVerifier().verify(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Throws IllegalArgumentException when token is blank")
        void should_ThrowIllegalArgumentException_when_TokenIsBlank() {
            assertThatThrownBy(() -> TestJwts.tokenVerifier().verify(" "))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Throws AuthenticationException when token is malformed")
        void should_ThrowAuthenticationException_when_TokenIsMalformed() {
            assertThatThrownBy(() -> TestJwts.tokenVerifier().verify("malformed-token"))
                    .isInstanceOf(AuthenticationException.class);
        }

        @Test
        @DisplayName("Throws AuthenticationException when token signature is invalid")
        void should_ThrowAuthenticationException_when_TokenSignatureIsInvalid() {
            var clock = TestClock.create();
            var properties = TestSecurityProperties.builder()
                    .withJwtSecret("invalid-secret")
                    .build();

            var verifier = TestJwts.tokenVerifier(properties, clock);

            assertThatThrownBy(() -> verifier.verify("invalid-token"))
                    .isInstanceOf(AuthenticationException.class);
        }
    }
}
