package com.example.todo.util;

import com.example.todo.dto.FieldErrorResponse;
import com.example.todo.enums.Priority;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdateTodoRequestValidator {
    public static List<FieldErrorResponse> validate(Map<String, Object> requestBody) {
        List<FieldErrorResponse> errors = new ArrayList<>();

        if (requestBody.containsKey("text")) {
            Object text = requestBody.get("text");

            if (!(text instanceof String textValue) || textValue.isEmpty()) {
                errors.add(new FieldErrorResponse("text", "Text must be a non-empty string.", text));
            } else if (textValue.length() < 3 || textValue.length() > 120) {
                errors.add(new FieldErrorResponse("text", "Text must be between 3 and 120 characters.", textValue));
            }
        }

        if (requestBody.containsKey("priority")) {
            Object priority = requestBody.get("priority");

            if (!(priority instanceof String priorityValue)) {
                errors.add(new FieldErrorResponse("priority", "Invalid value for priority. It must be one of: LOW, MEDIUM, HIGH.", priority));
            } else {
                try {
                    Priority.valueOf(priorityValue.toUpperCase());
                } catch (IllegalArgumentException e) {
                    errors.add(new FieldErrorResponse("priority", "Invalid value for priority. It must be one of: LOW, MEDIUM, HIGH.", priorityValue));
                }
            }
        }

        if (requestBody.containsKey("dueDate")) {
            Object dueDate = requestBody.get("dueDate");

            if (dueDate != null) {
                if (!(dueDate instanceof String dueDateString)) {
                    errors.add(new FieldErrorResponse("dueDate", "Invalid date format. Due date must be a valid LocalDateTime.", dueDate));
                } else {
                    try {
                        LocalDateTime dueDateValue = LocalDateTime.parse(dueDateString);

                        if (dueDateValue.isBefore(LocalDateTime.now())) {
                            errors.add(new FieldErrorResponse("dueDate", "Due date must be either today or in the future.", dueDateValue));
                        }
                    } catch (DateTimeParseException e) {
                        errors.add(new FieldErrorResponse("dueDate", "Invalid date format. Due date must be a valid LocalDateTime.", dueDate));
                    }
                }
            }
        }

        return errors;
    }
}
