package com.rewards.Rewards.exceptions;

import com.rewards.Rewards.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Boolean isStackTraceOn;

    public GlobalExceptionHandler(Environment env) {
        this.isStackTraceOn = Boolean.parseBoolean(env.getProperty("stacktrace.enabled", "false"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ErrorResponse> handleExternalServiceException(GeneralException ex) {
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Void> handleNoResourceFound(NoResourceFoundException ex, HttpServletRequest request) {
        String path = request.getRequestURI();
        if (path.endsWith("favicon.ico")) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(500).build();
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception exception, HttpStatus status) {
        String message = exception.getMessage() != null ? exception.getMessage() : "An unexpected error occurred.";

        if (status == HttpStatus.INTERNAL_SERVER_ERROR && !(exception instanceof GeneralException)) {
            message = "An unexpected error occurred. Please try again.";
            logger.error("Server error [{}]: {}", status.value(), message, exception);
        }

        ErrorResponse errorResponse = isStackTraceOn
                ? new ErrorResponse(status.value(), message, getStackTraceAsString(exception))
                : new ErrorResponse(status.value(), message);

        return ResponseEntity.status(status).body(errorResponse);
    }

    private String getStackTraceAsString(Exception exception) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : exception.getStackTrace()) {
            sb.append(element).append("\n");
        }
        return sb.toString();
    }
}
