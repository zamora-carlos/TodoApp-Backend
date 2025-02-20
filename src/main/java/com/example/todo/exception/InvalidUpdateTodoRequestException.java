package com.example.todo.exception;

import com.example.todo.dto.FieldErrorResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class InvalidUpdateTodoRequestException extends RuntimeException {
    private final List<FieldErrorResponse> errors;

    public InvalidUpdateTodoRequestException(List<FieldErrorResponse> errors) {
        super("Validation failed for update todo request.");
        this.errors = errors;
    }
}
