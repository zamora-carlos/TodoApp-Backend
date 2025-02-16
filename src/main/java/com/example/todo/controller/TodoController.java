package com.example.todo.controller;

import com.example.todo.dto.*;
import com.example.todo.enums.*;
import com.example.todo.mapper.TodoMapper;
import com.example.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todos")
@CrossOrigin("*")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping
    public ResponseEntity<PaginatedResponse<TodoResponse>> getAllTodos(
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "priority", required = false) Priority priority,
            @RequestParam(value = "done", required = false) Boolean done,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort_by", defaultValue = "TEXT") SortCriteria sortBy,
            @RequestParam(value = "order", defaultValue = "ASC") SortOrder order) {

        PaginatedResponse<TodoResponse> response = todoService.getTodos(
                text, priority, done, page, size, sortBy, order);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoResponse> getTodo(@PathVariable Long id) {
        TodoResponse todo = TodoMapper.toTodoResponse(todoService.getTodoById(id));
        return ResponseEntity.ok(todo);
    }

    @PostMapping
    public ResponseEntity<TodoResponse> createTodo(@Validated @RequestBody CreateTodoRequest todo) {
        TodoResponse createdTodo = todoService.createTodo(todo);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTodo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoResponse> updateTodo(
            @PathVariable Long id,
            @Validated @RequestBody UpdateTodoRequest updateTodoRequest) {

        TodoResponse updatedTodo = todoService.updateTodo(id, updateTodoRequest);
        return ResponseEntity.ok(updatedTodo);
    }

    @PutMapping("/{id}/done")
    public ResponseEntity<Void> markAsDone(@PathVariable Long id) {
        todoService.markAsDone(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/undone")
    public ResponseEntity<Void> markAsUndone(@PathVariable Long id) {
        todoService.markAsUndone(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/metrics")
    public ResponseEntity<MetricsResponse> getAverageCompletionTime() {
        return ResponseEntity.ok(todoService.getMetrics());
    }
}
