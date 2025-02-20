package com.example.todo.dto;

import com.example.todo.enums.Priority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class UpdateTodoRequest implements Serializable {
    private String text;
    private Priority priority;
    private LocalDateTime dueDate;

    @Accessors(fluent = true)
    private boolean updateDueDate;

    public UpdateTodoRequest(String text, Priority priority, LocalDateTime dueDate) {
        this.text = text;
        this.priority = priority;
        this.dueDate = dueDate;
    }
}
