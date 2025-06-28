package com.company.ws.error;

import com.company.ws.dto.error.ApiError;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class AllExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationError(MethodArgumentNotValidException exception) {
        ApiError apiError = new ApiError();
        apiError.setMessage("Validation Error");
        apiError.setStatusCode(400);
        Map<String, String> validationError = exception.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage,
                        (x, y) -> x));
        apiError.setValidationError(validationError);
        return ResponseEntity.status(400).body(apiError);
    }


    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiError> handleAuthException(AuthException exception) {
        ApiError apiError = new ApiError();
        apiError.setMessage(exception.getMessage());
        apiError.setStatusCode(401);
        return ResponseEntity.status(401).body(apiError);
    }


    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ApiError> handleMissingServletRequestPartException
            (MissingServletRequestPartException exception) {
        ApiError apiError = new ApiError();
        apiError.setMessage("File is empty");
        apiError.setStatusCode(400);
        return ResponseEntity.status(400).body(apiError);
    }

    @ExceptionHandler(ExpireTokenException.class)
    public ResponseEntity<ApiError> handleExpireTokenException(ExpireTokenException exception) {
        ApiError apiError = new ApiError();
        apiError.setMessage(exception.getMessage());
        apiError.setStatusCode(exception.getStatus());
        return ResponseEntity.status(exception.getStatus()).body(apiError);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiError> handleInvalidTokenException(InvalidTokenException exception) {
        ApiError apiError = new ApiError();
        apiError.setMessage(exception.getMessage());
        apiError.setStatusCode(400);
        return ResponseEntity.status(400).body(apiError);
    }


    @ExceptionHandler(MailSenderException.class)
    public ResponseEntity<ApiError> handleMailSenderException(MailSenderException exception) {
        ApiError apiError = new ApiError();
        apiError.setMessage(exception.getMessage());
        apiError.setStatusCode(502);
        return ResponseEntity.status(502).body(apiError);
    }


    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ApiError> handleCommonException(CommonException exception) {
        ApiError apiError = new ApiError();
        apiError.setMessage(exception.getMessage());
        apiError.setStatusCode(exception.getStatus());
        return ResponseEntity.status(exception.getStatus()).body(apiError);
    }


    @ExceptionHandler(UnknownFileTypeException.class)
    public ResponseEntity<ApiError> handleUnknownFileTypeException(UnknownFileTypeException exception) {
        ApiError apiError = new ApiError();
        apiError.setMessage(exception.getMessage());
        apiError.setStatusCode(400);
        return ResponseEntity.status(400).body(apiError);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException exception) {
        ApiError apiError = new ApiError();
        apiError.setMessage(exception.getMessage());
        apiError.setStatusCode(404);
        return ResponseEntity.status(404).body(apiError);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException exception) {
        ApiError apiError = new ApiError();
        apiError.setMessage(exception.getMessage());
        apiError.setStatusCode(401);
        return ResponseEntity.status(401).body(apiError);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(AuthorizationDeniedException exception) {
        ApiError apiError = new ApiError();
        apiError.setMessage(exception.getMessage());
        apiError.setStatusCode(403);
        return ResponseEntity.status(403).body(apiError);
    }

}
