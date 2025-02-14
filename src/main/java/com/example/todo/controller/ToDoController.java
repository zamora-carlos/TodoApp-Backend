package com.example.todo.controller;

import com.example.todo.model.ToDo;
import com.example.todo.dto.PaginatedResponse;
import com.example.todo.model.Priority;
import com.example.todo.service.ToDoService;
import com.example.todo.repository.ToDoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/todos")
public class ToDoController {

    @Autowired
    private ToDoService toDoService;
    @Autowired
    private ToDoRepository toDoRepository;

    @GetMapping
    public ResponseEntity<PaginatedResponse<ToDo>> getTodos(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "priority", required = false) Priority priority,
            @RequestParam(value = "done", required = false) Boolean done,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        List<ToDo> todos = toDoService.getTodos(name, priority, done, page, size);
        long totalItems = toDoRepository.findAll().size();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        PaginatedResponse<ToDo> response = new PaginatedResponse<>(todos, page, totalPages, totalItems);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ToDo createToDo(@RequestBody ToDo todo) {
        return toDoService.createToDo(todo);
    }

    @PutMapping("/{id}")
    public ToDo updateToDo(
            @PathVariable Long id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "priority", required = false) Priority priority,
            @RequestParam(value = "dueDate", required = false) LocalDateTime dueDate) {
        return toDoService.updateToDo(id, name, priority, dueDate);
    }

    @PostMapping("/{id}/done")
    public ResponseEntity<Void> markAsDone(@PathVariable Long id) {
        toDoService.markAsDone(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/undone")
    public ResponseEntity<Void> markAsUndone(@PathVariable Long id) {
        toDoService.markAsUndone(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/average-time")
    public double getAverageCompletionTime() {
        return toDoService.getAverageCompletionTime();
    }
}
