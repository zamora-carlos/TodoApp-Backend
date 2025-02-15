package com.example.todo.repository;

import com.example.todo.model.Todo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TodoRepository {
    private final List<Todo> todos = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong(1);

    public List<Todo> findAll() {
        return todos;
    }

    public Optional<Todo> findById(Long id) {
        return todos.stream()
                .filter(todo -> todo.getId().equals(id))
                .findFirst();
    }

    public Todo save(Todo todo) {
        if (todo.getId() != null) {
            for (int i = 0; i < todos.size(); i++) {
                if (todos.get(i).getId().equals(todo.getId())) {
                    todos.set(i, todo);
                    return todo;
                }
            }
        }

        long id = counter.getAndIncrement();
        todo.setId(id);
        todos.add(todo);

        return todo;
    }

    public void deleteById(Long id) {
        todos.removeIf(todo -> todo.getId().equals(id));
    }

    public void deleteAll() {
        todos.clear();
    }
}
