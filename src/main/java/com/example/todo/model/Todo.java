package com.example.todo.model;

import com.example.todo.enums.Priority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class Todo {
    private Long id;
    private String text;
    private Priority priority;
    private boolean isDone;
    private LocalDateTime dueDate;
    private LocalDateTime doneDate;
    private LocalDateTime createdAt;

    public Todo(String text, Priority priority) {
        this.text = text;
        this.priority = priority;
        this.isDone = false;
        this.createdAt = LocalDateTime.now();
    }

    public Todo(String text, Priority priority, LocalDateTime dueDate) {
        this.text = text;
        this.priority = priority;
        this.dueDate = dueDate;
        this.isDone = false;
        this.createdAt = LocalDateTime.now();
    }
}