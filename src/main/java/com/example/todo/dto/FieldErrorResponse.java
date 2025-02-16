package com.example.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class FieldErrorResponse implements Serializable {
    private String field;
    private String message;
    private Object rejectedValue;
}
