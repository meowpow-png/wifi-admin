package hr.ht.rnd.wifiadmin.infra.security;

import hr.ht.rnd.wifiadmin.infra.transport.rest.ErrorBodyDto;
import hr.ht.rnd.wifiadmin.infra.transport.rest.ErrorCode;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

import static hr.ht.rnd.wifiadmin.common.StructuredLog.Event;
import static hr.ht.rnd.wifiadmin.common.StructuredLog.debug;

@Component
final class ApiAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger log = LoggerFactory.getLogger(ApiAuthenticationEntryPoint.class);

    private final ObjectMapper objectMapper;

    ApiAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException ignored
    ) throws IOException {
        debug(log).withEvent(Event.AUTHENTICATION_FAILED)
                .withRequest(request)
                .log();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        var error = new ErrorBodyDto(
                "Authentication required",
                ErrorCode.AUTHENTICATION_FAILED
        );
        objectMapper.writeValue(
                response.getOutputStream(),
                error
        );
    }
}
