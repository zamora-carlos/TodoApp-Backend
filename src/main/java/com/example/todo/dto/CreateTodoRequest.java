package com.example.todo.dto;

import com.example.todo.enums.Priority;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class CreateTodoRequest implements Serializable {

    @NotNull(message = "Text is required.")
    @Size(min = 3, max = 120, message = "Text should be between 3 and 120 characters.")
    private String text;

    @NotNull(message = "Priority is required.")
    private Priority priority;

    @FutureOrPresent(message = "Due date must be either today or in the future.")
    private LocalDateTime dueDate;
}
