package hr.ht.rnd.wifiadmin.infra.rest;

import java.util.Objects;

/**
 * Represents an error returned to the REST client.
 *
 * @param message the human-readable error description
 * @param code the application-specific error identifier
 */
record ErrorBodyDto(String message, String code) {

    /**
     * Creates a REST error response.
     *
     * @param message the human-readable error description
     * @param code the application-specific error identifier
     *
     * @throws NullPointerException if any argument is {@code null}
     */
    ErrorBodyDto {
        Objects.requireNonNull(message, "message must not be null");
        Objects.requireNonNull(code, "code must not be null");
    }

    /**
     * Creates a REST error response.
     *
     * @param message the human-readable error description
     * @param code the application-specific error identifier
     *
     * @throws NullPointerException if any argument is {@code null}
     */
    ErrorBodyDto(String message, ErrorCode code) {
        this(message, code.name());
    }
}
