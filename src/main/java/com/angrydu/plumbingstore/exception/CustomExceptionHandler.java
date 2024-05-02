package com.angrydu.plumbingstore.exception;

import com.angrydu.plumbingstore.message.InternalizationMessageManagerConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.net.ConnectException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;

@ControllerAdvice
public class CustomExceptionHandler {

    public static final String RESOURCE_NOT_FOUND = "ExceptionHandler.ResourceNotFound";
    public static final String DATA_VALIDATION_ERROR = "ExceptionHandler.DataValidationError";
    public static final String INCORRECT_TYPE = "ExceptionHandler.IncorrectType";
    public static final String INCORRECT_USER_DATA = "ExceptionHandler.IncorrectUserData";
    public static final String INTERNAL_SERVER_ERROR = "ExceptionHandler.InternalServerError";

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotFoundException(CustomException ex) {
        HttpStatus httpStatus = switch (ex.getLocation()) {
            case TOKEN_SERVICE_CONFLICT -> HttpStatus.CONFLICT;
            case USER_SERVICE_VALIDATION, ORDER_SERVICE_VALIDATION, TOKEN_SERVICE_VALIDATION, USER_DTO_ENTITY_VALIDATION ->
                    HttpStatus.BAD_REQUEST;
            case USER_SERVICE_NOT_FOUND, TOKEN_NOT_FOUND, ORDER_SERVICE_NOT_FOUND, RESOURCE_NOT_FOUND ->
                    HttpStatus.NOT_FOUND;
            case TOKEN_FORBIDDEN -> HttpStatus.FORBIDDEN;
            case MAIL_ERROR, KEY_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
        return ResponseEntity.status(httpStatus).body(createExceptionResponse(ex.getLocalizedMessage(),
                InternalizationMessageManagerConfig.getExceptionMessage(ex.getLocation().toString())));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(createExceptionResponse(
                        InternalizationMessageManagerConfig.getExceptionMessage(RESOURCE_NOT_FOUND),
                        InternalizationMessageManagerConfig
                                .getExceptionMessage(ExceptionLocations.RESOURCE_NOT_FOUND.toString())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationException(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createExceptionResponse(
                        InternalizationMessageManagerConfig.getExceptionMessage(DATA_VALIDATION_ERROR),
                        InternalizationMessageManagerConfig
                                .getExceptionMessage(ExceptionLocations.USER_DTO_ENTITY_VALIDATION.toString())));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleMismatchException(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createExceptionResponse(InternalizationMessageManagerConfig.getExceptionMessage(INCORRECT_TYPE),
                        InternalizationMessageManagerConfig
                                .getExceptionMessage(ExceptionLocations.RESOURCE_NOT_FOUND.toString())));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(createExceptionResponse(
                        InternalizationMessageManagerConfig.getExceptionMessage(INCORRECT_USER_DATA),
                        InternalizationMessageManagerConfig
                                .getExceptionMessage(ExceptionLocations.TOKEN_FORBIDDEN.toString())));
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<ExceptionResponse> handleConnectExceptionException(ConnectException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createExceptionResponse(
                        InternalizationMessageManagerConfig.getExceptionMessage(INTERNAL_SERVER_ERROR),
                        InternalizationMessageManagerConfig
                                .getExceptionMessage(ExceptionLocations.MAIL_ERROR.toString())));
    }

    @ExceptionHandler(NoSuchAlgorithmException.class)
    public ResponseEntity<ExceptionResponse> handleNoSuchAlgorithmException(NoSuchAlgorithmException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createExceptionResponse(
                        InternalizationMessageManagerConfig.getExceptionMessage(INTERNAL_SERVER_ERROR),
                        InternalizationMessageManagerConfig
                                .getExceptionMessage(ExceptionLocations.KEY_ERROR.toString())));
    }

    @ExceptionHandler(InvalidKeySpecException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidKeySpecException(InvalidKeySpecException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createExceptionResponse(
                        InternalizationMessageManagerConfig.getExceptionMessage(INTERNAL_SERVER_ERROR),
                        InternalizationMessageManagerConfig
                                .getExceptionMessage(ExceptionLocations.KEY_ERROR.toString())));
    }

    private ExceptionResponse createExceptionResponse(String message, String exceptionLocations) {
        return new ExceptionResponse(message, exceptionLocations, LocalDateTime.now().toString());
    }
}
