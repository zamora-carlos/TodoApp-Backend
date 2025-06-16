package com.example.todo.util;

import com.example.todo.dto.FieldErrorResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DueDateValidator {

    public static List<FieldErrorResponse> validateDueDate(LocalDateTime dueDate) {
        List<FieldErrorResponse> errors = new ArrayList<>();

        if (dueDate != null) {
            LocalDate dueDateAsLocalDate = dueDate.toLocalDate();
            LocalDate today = LocalDate.now();

            if (dueDateAsLocalDate.isBefore(today)) {
                errors.add(new FieldErrorResponse("dueDate", "Due date must be either today or in the future.", dueDate));
            }
        }

        return errors;
    }
}