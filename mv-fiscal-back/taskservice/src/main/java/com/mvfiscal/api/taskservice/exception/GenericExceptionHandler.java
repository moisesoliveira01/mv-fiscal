package com.mvfiscal.api.taskservice.exception;

import com.mvfiscal.api.taskservice.http.ServerErrorApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class GenericExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GenericExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServerErrorApiResponse> handleException(Exception e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ServerErrorApiResponse(
                        e.getMessage()));
    }
}
