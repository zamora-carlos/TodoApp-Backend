package com.example.todo.controller;

import com.example.todo.model.Todo;
import com.example.todo.dto.PaginatedResponse;
import com.example.todo.model.Priority;
import com.example.todo.service.TodoService;
import com.example.todo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;
    @Autowired
    private TodoRepository todoRepository;

    @GetMapping
    public ResponseEntity<PaginatedResponse<Todo>> getTodos(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "priority", required = false) Priority priority,
            @RequestParam(value = "done", required = false) Boolean done,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        List<Todo> todos = todoService.getTodos(name, priority, done, page, size);
        long totalItems = todoRepository.findAll().size();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        PaginatedResponse<Todo> response = new PaginatedResponse<>(todos, page, totalPages, totalItems);
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
