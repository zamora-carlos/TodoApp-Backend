package com.example.todo.dto;

import com.example.todo.model.Priority;
import lombok.Data;

@Data
public class TodoFilter {
    private String name;
    private Priority priority;
    private Boolean done;

    public TodoFilter(String name, Priority priority, Boolean done) {
        this.name = (name != null) ? name.toLowerCase() : null;
        this.priority = priority;
        this.done = done;
    }
}
