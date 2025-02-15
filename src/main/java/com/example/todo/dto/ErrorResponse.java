package com.example.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse implements Serializable {
    private int code;
    private String message;
    private LocalDateTime timestamp;
}
