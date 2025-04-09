package com.mvfiscal.api.taskservice.exception;

import com.mvfiscal.api.taskservice.http.ValidationErrorApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(1)
public class ValidationExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ValidationExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorApiResponse> handleValidationExceptions(MethodArgumentNotValidException e) {
        StringBuilder errors = new StringBuilder();

        e.getBindingResult().getFieldErrors().forEach((error) -> {
            errors.append(error.getDefaultMessage()).append(", ");
        });
        String errorMessage = errors
                .toString()
                .trim()
                .substring(0, errors.length() - 2);
        logger.error(errorMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ValidationErrorApiResponse(
                        errorMessage));
    }

    @ExceptionHandler(TaskDataValidationException.class)
    public ResponseEntity<ValidationErrorApiResponse> handleUserDataValidationException(TaskDataValidationException e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ValidationErrorApiResponse(
                        e.getMessage()));
    }
}
