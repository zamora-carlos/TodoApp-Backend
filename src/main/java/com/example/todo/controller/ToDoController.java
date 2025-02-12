package com.example.todo.controller;

import com.example.todo.model.ToDo;
import com.example.todo.model.Priority;
import com.example.todo.service.ToDoService;
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

    @GetMapping
    public List<ToDo> getTodos(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "priority", required = false) Priority priority,
            @RequestParam(value = "done", required = false) Boolean done,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return toDoService.getTodos(name, priority, done, page, size);
    }

    // PENDING: Implement the logic to create a new ToDo
    //
    // @PostMapping
    // public ToDo createToDo(@RequestBody ToDo todo) { }

    @PutMapping("/{id}")
    public ToDo updateToDo(
            @PathVariable Long id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "priority", required = false) Priority priority,
            @RequestParam(value = "dueDate", required = false) LocalDateTime dueDate) {
        return toDoService.updateToDo(id, name, priority, dueDate);
    }

    // PENDING: Implement the logic to mark a ToDo as done
    //
    // @PostMapping("/{id}/done")
    // public ResponseEntity<Void> markAsDone(@PathVariable Long id) { }

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
