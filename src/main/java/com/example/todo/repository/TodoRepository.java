package com.example.todo.repository;

import com.example.todo.model.Todo;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TodoRepository {
    private final Map<Long, Todo> todos = new HashMap<>();
    private final AtomicLong counter = new AtomicLong(1);

    public List<Todo> findAll() {
        return new ArrayList<>(todos.values());
    }

    public Optional<Todo> findById(Long id) {
        return Optional.ofNullable(todos.get(id));
    }

    public Todo save(Todo todo) {
        if (todo.getId() == null) {
            todo.setId(counter.getAndIncrement());
        }

        todos.put(todo.getId(), todo);
        return todo;
    }

    public void deleteById(Long id) {
        todos.remove(id);
    }

    public void deleteAll() {
        todos.clear();
    }
}
