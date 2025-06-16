package com.example.todo.exception;

import com.example.todo.dto.FieldErrorResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class InvalidCreateTodoRequestException extends InvalidRequestException {
    public InvalidCreateTodoRequestException(List<FieldErrorResponse> errors) {
        super(errors);
    }
}