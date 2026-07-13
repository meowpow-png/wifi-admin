package hr.ht.rnd.wifiadmin.infra.security;

import hr.ht.rnd.wifiadmin.common.LogContext;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Opens a logging context for each HTTP request.
 * <p>
 * <strong>Implementation Note:</strong>
 * The logging context is established before request
 * processing begins so that all logs emitted during the request,
 * including authentication, request parsing, controller execution,
 * and exception handling, share the same trace identifier.
 */
final class TraceContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try (var ignored = LogContext.open()) {
            filterChain.doFilter(request, response);
        }
    }
}
