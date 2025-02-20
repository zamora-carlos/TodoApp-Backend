package com.example.todo.service;

import com.example.todo.dto.*;
import com.example.todo.enums.*;
import com.example.todo.exception.TodoNotFoundException;
import com.example.todo.mapper.TodoMapper;
import com.example.todo.model.Todo;
import com.example.todo.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public PaginatedResponse<TodoResponse> getTodos(
            String text,
            Priority priority,
            Boolean isDone,
            int page,
            int size,
            SortCriteria sortBy,
            SortOrder order) {

        List<Todo> filteredTodos = getFilteredTodos(
                todoRepository.findAll(), text != null ? text.toLowerCase() : null, priority, isDone);

        List<Todo> sortedTodos = getSortedTodos(filteredTodos, sortBy, order);

        List<TodoResponse> content = getPaginatedTodos(sortedTodos, page, size);

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
                        String.format("The todo with id %d was not found.", id)));
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

    private static List<Todo> getFilteredTodos(List<Todo> todos, String text, Priority priority, Boolean isDone) {
        return todos.stream()
                .filter(todo ->
                        (text == null || todo.getText().toLowerCase().contains(text)) &&
                        (priority == null || todo.getPriority() == priority) &&
                        (isDone == null || isDone.equals(todo.isDone())))
                .toList();
    }

    private static int compareDueDates(LocalDateTime date1, LocalDateTime date2) {
        if (date1 == null && date2 == null) return 0;
        if (date1 == null) return 1;
        if (date2 == null) return -1;

        return date1.compareTo(date2);
    }

    private static List<Todo> getSortedTodos(List<Todo> todos, SortCriteria sortBy, SortOrder order) {
        Comparator<Todo> comparator = switch (sortBy) {
            case TEXT -> Comparator
                    .comparing(Todo::getText,
                            order == SortOrder.ASC
                                    ? Comparator.naturalOrder()
                                    : Comparator.reverseOrder())
                    .thenComparing(Todo::getPriority, Comparator.reverseOrder())
                    .thenComparing((todo1, todo2) -> compareDueDates(todo1.getDueDate(), todo2.getDueDate()));
            case PRIORITY -> Comparator
                    .comparing(Todo::getPriority,
                            order == SortOrder.ASC
                                    ? Comparator.naturalOrder()
                                    : Comparator.reverseOrder())
                    .thenComparing((todo1, todo2) -> compareDueDates(todo1.getDueDate(), todo2.getDueDate()))
                    .thenComparing(Todo::getText);
            case DUE_DATE -> Comparator
                    .comparing(Todo::getDueDate,
                            order == SortOrder.ASC
                                    ? Comparator.nullsLast(LocalDateTime::compareTo)
                                    : Comparator.nullsLast(LocalDateTime::compareTo).reversed())
                    .thenComparing(Todo::getPriority, Comparator.reverseOrder())
                    .thenComparing(Todo::getText);
        };

        List<Todo> sortedTodos = new ArrayList<>(todos);
        sortedTodos.sort(comparator);

        return sortedTodos;
    }

    private static List<TodoResponse> getPaginatedTodos(List<Todo> todos, int page, int size) {
        return todos.stream()
                .skip((long) (page - 1) * size)
                .limit(size)
                .map(TodoMapper::toTodoResponse)
                .toList();
    }
}
