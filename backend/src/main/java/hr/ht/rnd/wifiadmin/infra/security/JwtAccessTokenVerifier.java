package hr.ht.rnd.wifiadmin.infra.security;

import hr.ht.rnd.wifiadmin.application.exception.AuthenticationException;
import hr.ht.rnd.wifiadmin.application.outbound.AccessTokenVerifier;

import io.jsonwebtoken.Clock;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.util.Objects;
import javax.crypto.SecretKey;

/**
 * Verifies JWT access tokens.
 */
final class JwtAccessTokenVerifier implements AccessTokenVerifier {

    private final SecurityProperties properties;
    private final Clock clock;

    JwtAccessTokenVerifier(SecurityProperties properties, Clock clock) {
        this.properties = properties;
        this.clock = clock;
    }

    @Override
    public String verify(String token) {
        Objects.requireNonNull(token, "token must not be null");
        if (token.isBlank()) {
            throw new IllegalArgumentException("token must not be blank");
        }
        try {
            return Jwts.parser()
                    .clock(clock)
                    .verifyWith(signingKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        }
        catch (JwtException e) {
            throw new AuthenticationException(e);
        }
    }

    private SecretKey signingKey() {
        var key = Decoders.BASE64.decode(properties.jwtSecret());
        return Keys.hmacShaKeyFor(key);
    }
}
