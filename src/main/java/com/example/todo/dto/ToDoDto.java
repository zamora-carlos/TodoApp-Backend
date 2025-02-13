package com.example.todo.dto;

import com.example.todo.model.Priority;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class ToDoDto implements Serializable {
    private Long id;
    private String text;
    private LocalDateTime dueDate;
    private Priority priority;
    private boolean isDone;
}
