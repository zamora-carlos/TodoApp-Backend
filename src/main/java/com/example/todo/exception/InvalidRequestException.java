package com.example.todo.exception;

import com.example.todo.dto.FieldErrorResponse;
import lombok.Getter;

import java.util.List;

@Getter
public abstract class InvalidRequestException extends RuntimeException {
    private final List<FieldErrorResponse> errors;

    protected InvalidRequestException(List<FieldErrorResponse> errors) {
        this.errors = errors;
    }
}

