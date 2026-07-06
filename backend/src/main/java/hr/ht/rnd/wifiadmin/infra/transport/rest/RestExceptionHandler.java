package hr.ht.rnd.wifiadmin.infra.transport.rest;

import hr.ht.rnd.wifiadmin.application.exception.*;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static hr.ht.rnd.wifiadmin.common.StructuredLog.*;

@RestControllerAdvice
class RestExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    ErrorBodyDto handleAuthenticationFailure(
            AuthenticationException ignored,
            HttpServletRequest request
    ) {
        debug(log).withEvent(Event.AUTHENTICATION_FAILED)
                .withRequest(request)
                .log();

        return new ErrorBodyDto(
                "Authentication failed",
                ErrorCode.AUTHENTICATION_FAILED
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ErrorBodyDto handleValidationFailure(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        var message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Request validation failed");

        debug(log).withEvent(Event.REQUEST_VALIDATION_FAILED)
                .withField(Field.VALIDATION_MESSAGE, message)
                .withRequest(request)
                .log();

        return new ErrorBodyDto(message, ErrorCode.VALIDATION_FAILED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidRequestException.class)
    ErrorBodyDto handleInvalidRequest(
            InvalidRequestException exception,
            HttpServletRequest request
    ) {
        var message = exception.getMessage();
        debug(log).withEvent(Event.INVALID_REQUEST)
                .withField(Field.VALIDATION_MESSAGE, message)
                .withRequest(request)
                .log();

        return new ErrorBodyDto(message, ErrorCode.VALIDATION_FAILED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    ErrorBodyDto handleRequestParsingFailure(
            HttpMessageNotReadableException exception,
            HttpServletRequest request
    ) {
        debug(log).withEvent(Event.REQUEST_BODY_PARSE_FAILED)
                .withRequest(request)
                .withCause(exception)
                .log();

        return new ErrorBodyDto(
                "Invalid request body",
                ErrorCode.VALIDATION_FAILED
        );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CpeNotFoundException.class)
    ErrorBodyDto handleCpeNotFound(
            CpeNotFoundException exception,
            HttpServletRequest request
    ) {
        debug(log).withEvent(Event.CPE_NOT_FOUND)
                .withField(Field.CPE_ID, exception.cpeId())
                .withRequest(request)
                .log();

        return new ErrorBodyDto(
                "CPE not found",
                ErrorCode.CPE_NOT_FOUND
        );
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(PlatformResponseException.class)
    ErrorBodyDto handleInvalidPlatformResponse(PlatformResponseException exception) {
        error(log).withEvent(Event.PLATFORM_RESPONSE_INVALID)
                .withCause(exception)
                .log();

        return new ErrorBodyDto(
                "Invalid platform response",
                ErrorCode.PLATFORM_ERROR
        );
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(PlatformTransportException.class)
    ErrorBodyDto handlePlatformTransportFailure(PlatformTransportException exception) {
        error(log).withEvent(Event.PLATFORM_COMMUNICATION_FAILED)
                .withCause(exception)
                .log();

        return new ErrorBodyDto(
                "Platform communication failed",
                ErrorCode.PLATFORM_ERROR
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(AccountNotFoundException.class)
    ErrorBodyDto handleAccountNotFound(AccountNotFoundException exception) {
        error(log).withEvent(Event.ADMINISTRATOR_ACCOUNT_NOT_FOUND)
                .withCause(exception)
                .log();

        return new ErrorBodyDto(
                "Internal server error",
                ErrorCode.INTERNAL_SERVER_ERROR
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    ErrorBodyDto handleUnexpectedException(
            Exception exception,
            HttpServletRequest request
    ) {
        error(log).withEvent(Event.UNHANDLED_EXCEPTION)
                .withRequest(request)
                .withCause(exception)
                .log();

        return new ErrorBodyDto(
                "Internal server error",
                ErrorCode.INTERNAL_SERVER_ERROR
        );
    }
}
