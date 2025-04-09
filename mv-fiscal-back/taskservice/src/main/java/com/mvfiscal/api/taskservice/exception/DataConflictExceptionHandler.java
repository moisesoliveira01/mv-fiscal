package com.mvfiscal.api.taskservice.exception;

import com.mvfiscal.api.taskservice.http.ValidationErrorApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(1)
public class DataConflictExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(DataConflictExceptionHandler.class);

    @ExceptionHandler(TaskDataConflictException.class)
    public ResponseEntity<ValidationErrorApiResponse> handleTaskDataConflictException(TaskDataConflictException e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ValidationErrorApiResponse(
                        e.getMessage()));
    }
}
