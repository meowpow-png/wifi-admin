package hr.ht.rnd.wifiadmin.infra.security;

import hr.ht.rnd.wifiadmin.application.exception.AuthenticationException;
import hr.ht.rnd.wifiadmin.application.outbound.AccessTokenIssuer;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import javax.crypto.SecretKey;

/**
 * Issues JWT access tokens.
 * <p>
 * <strong>Implementation Note:</strong>
 * Issued token contains only the administrator username
 * because the application supports a single administrator role.
 */
@Component
final class JwtAccessTokenIssuer implements AccessTokenIssuer {

    private final SecurityProperties properties;
    private final Clock clock;

    JwtAccessTokenIssuer(SecurityProperties properties, Clock clock) {
        this.properties = properties;
        this.clock = clock;
    }

    @Override
    public String issue(String username) {
        Objects.requireNonNull(username, "username must not be null");
        if (username.isBlank()) {
            throw new IllegalArgumentException("username must not be blank");
        }
        var now = Instant.now(clock);
        try {
            return Jwts.builder()
                    .subject(username)
                    .issuedAt(Date.from(now))
                    .expiration(Date.from(now.plus(properties.jwtExpiration())))
                    .signWith(signingKey())
                    .compact();
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
