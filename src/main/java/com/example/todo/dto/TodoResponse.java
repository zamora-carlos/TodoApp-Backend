package com.example.todo.dto;

import com.example.todo.model.Priority;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class TodoResponse implements Serializable {
    private Long id;
    private String text;
    private Priority priority;
    private boolean isDone;
    private LocalDateTime dueDate;

    public TodoResponse(Long id, String text, Priority priority, boolean isDone, LocalDateTime dueDate) {
        this.id = id;
        this.text = text;
        this.priority = priority;
        this.isDone = isDone;
        this.dueDate = dueDate;
    }
}
