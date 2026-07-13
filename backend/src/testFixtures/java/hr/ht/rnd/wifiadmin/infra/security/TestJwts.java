package hr.ht.rnd.wifiadmin.infra.security;

import hr.ht.rnd.wifiadmin.application.outbound.AccessTokenIssuer;
import hr.ht.rnd.wifiadmin.application.outbound.AccessTokenVerifier;
import hr.ht.rnd.wifiadmin.infra.app.TestClock;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.FixedClock;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.time.Clock;
import java.util.Date;

public final class TestJwts {

    private TestJwts() {}

    public static AccessTokenIssuer tokenIssuer(
            SecurityProperties properties,
            Clock clock
    ) {
        return new JwtAccessTokenIssuer(properties, clock);
    }

    public static AccessTokenIssuer tokenIssuer() {
        return tokenIssuer(
                TestSecurityProperties.builder().build(),
                TestClock.create()
        );
    }

    public static AccessTokenVerifier tokenVerifier(
            SecurityProperties properties,
            Clock clock
    ) {
        return new JwtAccessTokenVerifier(
                properties,
                () -> Date.from(clock.instant())
        );
    }

    public static AccessTokenVerifier tokenVerifier() {
        return tokenVerifier(
                TestSecurityProperties.builder().build(),
                TestClock.create()
        );
    }

    public static JwtParser parser(Clock clock, String secret) {
        var decodedSecret = Decoders.BASE64.decode(secret);
        return Jwts.parser()
                .clock(new FixedClock(Date.from(clock.instant())))
                .verifyWith(Keys.hmacShaKeyFor(decodedSecret))
                .build();
    }
}
