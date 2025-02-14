package com.example.todo.controller;

import com.example.todo.dto.PaginatedResponse;
import com.example.todo.dto.TodoFilter;
import com.example.todo.dto.TodoResponse;
import com.example.todo.model.Priority;
import com.example.todo.model.Todo;
import com.example.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping
    public ResponseEntity<PaginatedResponse<TodoResponse>> getAllTodos(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "priority", required = false) Priority priority,
            @RequestParam(value = "done", required = false) Boolean done,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        TodoFilter filter = new TodoFilter(name, priority, done);
        PaginatedResponse<TodoResponse> response = todoService.getTodos(filter, page, size);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public Todo createTodo(@RequestBody Todo todo) {
        return todoService.createTodo(todo);
    }

    @PutMapping("/{id}")
    public Todo updateTodo(
            @PathVariable Long id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "priority", required = false) Priority priority,
            @RequestParam(value = "dueDate", required = false) LocalDateTime dueDate) {
        return todoService.updateTodo(id, name, priority, dueDate);
    }

    @PostMapping("/{id}/done")
    public ResponseEntity<Void> markAsDone(@PathVariable Long id) {
        todoService.markAsDone(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/undone")
    public ResponseEntity<Void> markAsUndone(@PathVariable Long id) {
        todoService.markAsUndone(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/average-time")
    public double getAverageCompletionTime() {
        return todoService.getAverageCompletionTime();
    }
}
