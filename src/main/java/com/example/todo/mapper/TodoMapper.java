package com.example.todo.mapper;

import com.example.todo.dto.CreateTodoRequest;
import com.example.todo.dto.TodoResponse;
import com.example.todo.dto.UpdateTodoRequest;
import com.example.todo.model.Todo;

public class TodoMapper {
    public static TodoResponse toTodoResponse(Todo todo) {
        return TodoResponse.builder()
                .id(todo.getId())
                .text(todo.getText())
                .priority(todo.getPriority())
                .isDone(todo.isDone())
                .dueDate(todo.getDueDate())
                .build();
    }

    public static CreateTodoRequest toCreateTodoRequest(Todo todo) {
        return CreateTodoRequest.builder()
                .text(todo.getText())
                .priority(todo.getPriority())
                .dueDate(todo.getDueDate())
                .build();
    }

    public static UpdateTodoRequest toUpdateTodoRequest(Todo todo) {
        return UpdateTodoRequest.builder()
                .text(todo.getText())
                .priority(todo.getPriority())
                .dueDate(todo.getDueDate())
                .build();
    }
}
