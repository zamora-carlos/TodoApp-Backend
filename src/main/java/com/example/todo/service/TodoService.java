package com.example.todo.service;

import com.example.todo.dto.CreateTodoRequest;
import com.example.todo.dto.PaginatedResponse;
import com.example.todo.dto.TodoFilter;
import com.example.todo.dto.TodoResponse;
import com.example.todo.model.Priority;
import com.example.todo.model.Todo;
import com.example.todo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    public PaginatedResponse<TodoResponse> getTodos(TodoFilter filter, int page, int size) {
        List<Todo> filteredTodos = todoRepository.findAll()
                .stream()
                .filter(todo ->
                        (filter.getName() == null || todo.getText().toLowerCase().contains(filter.getName())) &&
                        (filter.getPriority() == null || todo.getPriority() == filter.getPriority()) &&
                        (filter.getDone() == null || todo.isDone() == filter.getDone())).toList();

        List<TodoResponse> content = filteredTodos.stream()
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

        long totalItems = filteredTodos.size();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        return PaginatedResponse.<TodoResponse>builder()
                .content(content)
                .currentPage(page)
                .totalPages(totalPages)
                .pageSize(size)
                .totalItems(totalItems)
                .filter(filter)
                .build();
    }

    public TodoResponse createTodo(CreateTodoRequest createTodoRequest) {
        Todo todo = new Todo(createTodoRequest.getText(), createTodoRequest.getPriority());
        todo.setDueDate(createTodoRequest.getDueDate());

        Todo createdTodo = todoRepository.save(todo);

        return TodoResponse.builder()
                .id(createdTodo.getId())
                .text(createdTodo.getText())
                .priority(createdTodo.getPriority())
                .isDone(createdTodo.isDone())
                .dueDate(createdTodo.getDueDate())
                .build();
    }

    public Todo updateTodo(Long id, String name, Priority priority, LocalDateTime dueDate) {
        Optional<Todo> existingTodo = todoRepository.findById(id);
        if (existingTodo.isPresent()) {
            Todo todo = existingTodo.get();
            todo.setText(name);
            todo.setPriority(priority);
            todo.setDueDate(dueDate);
            return todoRepository.save(todo);
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
        return todoRepository.findAll()
                .stream()
                .filter(Todo::isDone)
                .map(todo -> Duration.between(todo.getCreatedAt(), todo.getDoneDate()).getSeconds())
                .reduce(Long::sum).orElse(0L);
    }
}
