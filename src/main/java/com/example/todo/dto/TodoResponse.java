package com.example.todo.dto;

import com.example.todo.enums.Priority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class TodoResponse implements Serializable {
    private Long id;
    private String text;
    private Priority priority;
    private boolean isDone;
    private LocalDateTime dueDate;
}
