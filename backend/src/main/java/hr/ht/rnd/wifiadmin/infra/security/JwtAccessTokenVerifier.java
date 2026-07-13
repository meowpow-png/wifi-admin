package hr.ht.rnd.wifiadmin.infra.security;

import hr.ht.rnd.wifiadmin.application.exception.AuthenticationException;
import hr.ht.rnd.wifiadmin.application.outbound.AccessTokenVerifier;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.util.Objects;
import javax.crypto.SecretKey;

/**
 * Verifies JWT access tokens.
 */
@Component
final class JwtAccessTokenVerifier implements AccessTokenVerifier {

    private final SecurityProperties properties;

    JwtAccessTokenVerifier(SecurityProperties properties) {
        this.properties = properties;
    }

    @Override
    public String verify(String token) {
        Objects.requireNonNull(token, "token must not be null");
        if (token.isBlank()) {
            throw new IllegalArgumentException("token must not be blank");
        }
        try {
            return Jwts.parser()
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
