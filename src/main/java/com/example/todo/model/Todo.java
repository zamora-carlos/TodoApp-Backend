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

    @Builder.Default
    private boolean isDone = false;

    private LocalDateTime dueDate;
    private LocalDateTime doneDate;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public Todo(String text, Priority priority, LocalDateTime dueDate) {
        this.text = text;
        this.priority = priority;
        this.isDone = false;
        this.dueDate = dueDate;
        this.createdAt = LocalDateTime.now();
    }

    public Todo(String text, Priority priority) {
        this(text, priority, null);
    }
}