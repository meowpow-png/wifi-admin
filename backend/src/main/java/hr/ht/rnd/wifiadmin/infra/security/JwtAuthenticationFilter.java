package hr.ht.rnd.wifiadmin.infra.security;

import hr.ht.rnd.wifiadmin.application.exception.AuthenticationException;
import hr.ht.rnd.wifiadmin.application.outbound.AccessTokenVerifier;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;

/**
 * Authenticates requests using JWT access tokens.
 * <p>
 * <strong>Implementation Note:</strong>
 * Authentication failures are intentionally ignored
 * and the filter chain continues. Spring Security
 * enforces authorization later based on the
 * current {@code SecurityContext}.
 */
@Component
class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final WebAuthenticationDetailsSource DETAILS_SOURCE =
            new WebAuthenticationDetailsSource();

    private final AccessTokenVerifier verifier;
    private final UserDetailsService service;

    JwtAuthenticationFilter(AccessTokenVerifier verifier, UserDetailsService service) {
        Objects.requireNonNull(verifier, "verifier must not be null");
        Objects.requireNonNull(service, "service must not be null");

        this.verifier = verifier;
        this.service = service;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        var context = SecurityContextHolder.getContext();

        if (context.getAuthentication() == null) {
            var authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
            var token = bearerToken(authorization);

            if (token != null) {
                authenticateRequest(context, request, token);
            }
        }
        filterChain.doFilter(request, response);
    }

    private void authenticateRequest(
            SecurityContext context,
            HttpServletRequest request,
            String token
    ) {
        try {
            var username = verifier.verify(token);
            var user = service.loadUserByUsername(username);

            var authentication = UsernamePasswordAuthenticationToken.authenticated(
                    user,
                    null,
                    user.getAuthorities()
            );
            var details = DETAILS_SOURCE.buildDetails(request);
            authentication.setDetails(details);

            context.setAuthentication(authentication);
        }
        catch (AuthenticationException | UsernameNotFoundException ignored) {
            SecurityContextHolder.clearContext();
        }
    }

    @Nullable
    private static String bearerToken(@Nullable String authorization) {
        if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
            return null;
        }
        return authorization.substring(BEARER_PREFIX.length());
    }
}
