package hr.ht.rnd.wifiadmin.infra.transport.rest;

import hr.ht.rnd.wifiadmin.application.outbound.CpeNotFoundException;
import hr.ht.rnd.wifiadmin.application.outbound.PlatformResponseException;
import hr.ht.rnd.wifiadmin.application.outbound.PlatformTransportException;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
class RestExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ErrorBodyDto handleValidationFailure(MethodArgumentNotValidException exception) {
        var message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Request validation failed");

        log.warn("Request validation failed: {}", message);

        return new ErrorBodyDto(message, ErrorCode.VALIDATION_FAILED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidRequestException.class)
    ErrorBodyDto handleInvalidRequest(InvalidRequestException exception) {
        var message = exception.getMessage();
        log.warn("Invalid request: {}", message);

        return new ErrorBodyDto(message, ErrorCode.VALIDATION_FAILED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    ErrorBodyDto handleRequestParsingFailure(HttpMessageNotReadableException ignored) {
        log.warn("Failed to parse request body", ignored);

        return new ErrorBodyDto(
                "Invalid request body",
                ErrorCode.VALIDATION_FAILED
        );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CpeNotFoundException.class)
    ErrorBodyDto handleCpeNotFound(CpeNotFoundException ignored) {
        return new ErrorBodyDto(
                "CPE not found",
                ErrorCode.CPE_NOT_FOUND
        );
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(PlatformResponseException.class)
    ErrorBodyDto handleInvalidPlatformResponse(PlatformResponseException ignored) {
        log.error("SOAP platform returned an invalid response", ignored);

        return new ErrorBodyDto(
                "Invalid platform response",
                ErrorCode.PLATFORM_ERROR
        );
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(PlatformTransportException.class)
    ErrorBodyDto handlePlatformTransportFailure(PlatformTransportException ignored) {
        log.error("SOAP platform communication failed", ignored);

        return new ErrorBodyDto(
                "Platform communication failed",
                ErrorCode.PLATFORM_ERROR
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    ErrorBodyDto handleUnexpectedException(Exception ignored) {
        log.error("Unhandled exception", ignored);

        return new ErrorBodyDto(
                "Internal server error",
                ErrorCode.INTERNAL_SERVER_ERROR
        );
    }
}
