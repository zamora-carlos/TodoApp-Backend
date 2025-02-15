package com.example.todo.mapper;

import com.example.todo.dto.CreateTodoRequest;
import com.example.todo.dto.TodoResponse;
import com.example.todo.dto.UpdateTodoRequest;
import com.example.todo.model.Todo;

import java.time.LocalDateTime;

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

    public static Todo createTodoFromRequest(CreateTodoRequest createTodoRequest) {
        return Todo.builder()
                .text(createTodoRequest.getText())
                .priority(createTodoRequest.getPriority())
                .dueDate(createTodoRequest.getDueDate())
                .isDone(false)
                .createdAt(LocalDateTime.now())
                .build();
    }


    public static UpdateTodoRequest toUpdateTodoRequest(Todo todo) {
        return UpdateTodoRequest.builder()
                .text(todo.getText())
                .priority(todo.getPriority())
                .dueDate(todo.getDueDate())
                .build();
    }

    public static Todo updateTodoFromRequest(Todo todo, UpdateTodoRequest updateTodoRequest) {
        todo.setText(updateTodoRequest.getText());
        todo.setPriority(updateTodoRequest.getPriority());
        todo.setDueDate(updateTodoRequest.getDueDate());
        return todo;
    }
}
