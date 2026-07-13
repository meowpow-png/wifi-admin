package hr.ht.rnd.wifiadmin.infra.transport.rest;

/**
 * Error codes returned to the REST client.
 */
public enum ErrorCode {

    VALIDATION_FAILED,
    CPE_NOT_FOUND,
    PLATFORM_ERROR,
    AUTHENTICATION_FAILED,
    INTERNAL_SERVER_ERROR
}
