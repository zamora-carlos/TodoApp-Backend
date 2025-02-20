package com.example.todo.controller;

import com.example.todo.dto.*;
import com.example.todo.enums.*;
import com.example.todo.exception.InvalidUpdateTodoRequestException;
import com.example.todo.mapper.TodoMapper;
import com.example.todo.service.TodoService;
import com.example.todo.util.UpdateTodoRequestValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/todos")
@Tag(name = "Todo", description = "Manage your todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    @Operation(summary = "Get all todos", description = "Fetches a list of todos, with optional filters and pagination.")
    public ResponseEntity<PaginatedResponse<TodoResponse>> getAllTodos(
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "priority", required = false) Priority priority,
            @RequestParam(value = "done", required = false) Boolean done,
            @RequestParam(value = "page", defaultValue = "1") @Min(1) int page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) int size,
            @RequestParam(value = "sort_by", defaultValue = "TEXT") SortCriteria sortBy,
            @RequestParam(value = "order", defaultValue = "ASC") SortOrder order) {

        PaginatedResponse<TodoResponse> response = todoService.getTodos(
                text, priority, done, page, size, sortBy, order);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get todo by ID", description = "Retrieve a specific todo item by its ID.")
    public ResponseEntity<TodoResponse> getTodo(@PathVariable Long id) {
        TodoResponse todo = TodoMapper.toTodoResponse(todoService.getTodoById(id));
        return ResponseEntity.ok(todo);
    }

    @Operation(summary = "Create a new todo", description = "Create a new todo item with the provided details.")
    @PostMapping
    public ResponseEntity<TodoResponse> createTodo(@Validated @RequestBody CreateTodoRequest todo) {
        TodoResponse createdTodo = todoService.createTodo(todo);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTodo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update todo by ID", description = "Update an existing todo item by its ID.")
    public ResponseEntity<TodoResponse> updateTodo(
            @PathVariable Long id,
            @RequestBody Map<String, Object> requestBody) {

        List<FieldErrorResponse> errors = UpdateTodoRequestValidator.validate(requestBody);

        if (!errors.isEmpty()) {
            throw new InvalidUpdateTodoRequestException(errors);
        }

        UpdateTodoRequest updateTodoRequest = UpdateTodoRequest.builder()
                .text(requestBody.get("text") != null && !((String) requestBody.get("text")).isEmpty() ? (String) requestBody.get("text") : null)
                .priority(requestBody.get("priority") != null ? Priority.valueOf((String) requestBody.get("priority")) : null)
                .dueDate(requestBody.get("dueDate") != null ? LocalDateTime.parse((String) requestBody.get("dueDate")) : null)
                .updateDueDate(requestBody.containsKey("dueDate"))
                .build();

        TodoResponse updatedTodo = todoService.updateTodo(id, updateTodoRequest);

        return ResponseEntity.ok(updatedTodo);
    }

    @PutMapping("/{id}/done")
    @Operation(summary = "Mark todo as done", description = "Mark a todo item as completed.")
    public ResponseEntity<Void> markAsDone(@PathVariable Long id) {
        todoService.markAsDone(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/undone")
    @Operation(summary = "Mark todo as undone", description = "Mark a todo item as incomplete.")
    public ResponseEntity<Void> markAsUndone(@PathVariable Long id) {
        todoService.markAsUndone(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete todo by ID", description = "Delete a specific todo item by its ID.")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/metrics")
    @Operation(summary = "Get todo metrics", description = "Retrieve the average completion time in seconds for each priority.")
    public ResponseEntity<MetricsResponse> getAverageCompletionTime() {
        return ResponseEntity.ok(todoService.getMetrics());
    }
}
