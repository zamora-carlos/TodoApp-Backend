package com.example.todo.service;

import com.example.todo.dto.PaginatedResponse;
import com.example.todo.dto.TodoResponse;
import com.example.todo.model.Priority;
import com.example.todo.model.Todo;
import com.example.todo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    public PaginatedResponse<TodoResponse> getTodos(String name, Priority priority, Boolean done, int page, int size) {
        List<Todo> todos = todoRepository.findAll();

        List<TodoResponse> content = todos.stream()
                .filter(todo ->
                        (name == null || todo.getText().toLowerCase().contains(name)) &&
                        (priority == null || todo.getPriority() == priority) &&
                        (done == null || todo.isDone() == done))
                .skip((long) (page - 1) * size)
                .limit(size)
                .map(todo -> TodoResponse.builder()
                        .id(todo.getId())
                        .text(todo.getText())
                        .priority(todo.getPriority())
                        .isDone(todo.isDone())
                        .dueDate(todo.getDueDate())
                        .build())
                .toList();

        long totalItems = todos.size();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        return PaginatedResponse.<TodoResponse>builder()
                .content(content)
                .currentPage(page)
                .totalPages(totalPages)
                .pageSize(size)
                .totalItems(totalItems)
                .build();
    }

    public Todo createTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    public Todo updateTodo(Long id, String name, Priority priority, LocalDateTime dueDate) {
        Optional<Todo> existingTodo = todoRepository.findById(id);
        if (existingTodo.isPresent()) {
            Todo toDo = existingTodo.get();
            toDo.setText(name);
            toDo.setPriority(priority);
            toDo.setDueDate(dueDate);
            return todoRepository.save(toDo);
        }
        return null;
    }

    public void markAsDone(Long id) {
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        if (optionalTodo.isPresent() && !optionalTodo.get().isDone()) {
            Todo todo = optionalTodo.get();
            todo.setDone(true);
            todo.setDoneDate(LocalDateTime.now());
            todoRepository.save(todo);
        }
    }

    public void markAsUndone(Long id) {
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        if (optionalTodo.isPresent() && optionalTodo.get().isDone()) {
            Todo todo = optionalTodo.get();
            todo.setDone(false);
            todo.setDoneDate(null);
            todoRepository.save(todo);
        }
    }

    public long getAverageCompletionTime() {
        // Logic to calculate average time between creation and done
        return 0L; // Return the calculated value
    }
}
