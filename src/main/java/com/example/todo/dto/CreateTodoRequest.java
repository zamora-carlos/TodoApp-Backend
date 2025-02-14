package com.example.todo.dto;

import com.example.todo.model.Priority;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class CreateTodoRequest implements Serializable {
    private String text;
    private Priority priority;
    private LocalDateTime dueDate;

    public CreateTodoRequest(String text, Priority priority, LocalDateTime dueDate) {
        this.text = text;
        this.priority = priority;
        this.dueDate = dueDate;
    }
}
