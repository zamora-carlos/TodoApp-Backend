package com.example.todo.service;

import com.example.todo.dto.*;
import com.example.todo.enums.*;
import com.example.todo.exception.TodoNotFoundException;
import com.example.todo.mapper.TodoMapper;
import com.example.todo.model.Todo;
import com.example.todo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    public PaginatedResponse<TodoResponse> getTodos(
            String text,
            Priority priority,
            Boolean isDone,
            int page,
            int size,
            SortCriteria sortBy,
            SortOrder order) {
        List<Todo> filteredTodos = todoRepository.findAll()
                .stream()
                .filter(todo ->
                        (text == null || todo.getText().toLowerCase().contains(text.toLowerCase())) &&
                        (priority == null || todo.getPriority().equals(priority)) &&
                        (isDone == null || isDone.equals(todo.isDone())))
                .toList();

        List<TodoResponse> content = filteredTodos.stream()
                .skip((long) (page - 1) * size)
                .limit(size)
                .map(TodoMapper::toTodoResponse)
                .toList();

        long totalItems = filteredTodos.size();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        return PaginatedResponse.<TodoResponse>builder()
                .content(content)
                .currentPage(page)
                .totalPages(totalPages)
                .pageSize(size)
                .totalItems(totalItems)
                .build();
    }

    public Todo getTodoById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(
                        String.format("The todo with id %d was not found", id)));
    }

    public TodoResponse createTodo(CreateTodoRequest createTodoRequest) {
        Todo todo = TodoMapper.createTodoFromRequest(createTodoRequest);
        return TodoMapper.toTodoResponse(todoRepository.save(todo));
    }

    public TodoResponse updateTodo(Long id, UpdateTodoRequest updateTodoRequest) {
        Todo todo = getTodoById(id);
        TodoMapper.updateTodoFromRequest(todo, updateTodoRequest);

        return TodoMapper.toTodoResponse(todoRepository.save(todo));
    }

    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }

    public void markAsDone(Long id) {
        Todo todo = getTodoById(id);

        if (!todo.isDone()) {
            todo.setDone(true);
            todo.setDoneDate(LocalDateTime.now());
            todoRepository.save(todo);
        }
    }

    public void markAsUndone(Long id) {
        Todo todo = getTodoById(id);

        if (todo.isDone()) {
            todo.setDone(false);
            todo.setDoneDate(null);
            todoRepository.save(todo);
        }
    }

    public MetricsResponse getMetrics() {
        return MetricsResponse.builder()
                .avgTime(getAverageCompletionTimeByPriority(null))
                .avgTimeLow(getAverageCompletionTimeByPriority(Priority.LOW))
                .avgTimeMedium(getAverageCompletionTimeByPriority(Priority.MEDIUM))
                .avgTimeHigh(getAverageCompletionTimeByPriority(Priority.HIGH))
                .build();
    }

    private long getAverageCompletionTimeByPriority(Priority priority) {
        List<Todo> filteredTodos = todoRepository.findAll()
                .stream()
                .filter(todo -> todo.isDone() && (priority == null || todo.getPriority() == priority))
                .toList();

        long totalTime = filteredTodos.stream()
                .mapToLong(todo -> Duration.between(todo.getCreatedAt(), todo.getDoneDate()).getSeconds())
                .sum();

        return filteredTodos.isEmpty() ? 0L : Math.round((double) totalTime / filteredTodos.size());
    }
}
