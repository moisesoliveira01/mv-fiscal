package com.mvfiscal.api.taskservice.exception;

public class TaskDataConflictException extends RuntimeException {
    public TaskDataConflictException(String message) {
        super(message);
    }
}
