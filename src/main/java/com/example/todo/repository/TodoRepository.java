package com.example.todo.repository;

import com.example.todo.model.Todo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TodoRepository {

    private final List<Todo> todos = new ArrayList<>();

    public List<Todo> findAll() {
        return todos;
    }

    public Optional<Todo> findById(Long id) {
        return todos.stream()
                .filter(todo -> todo.getId().equals(id))
                .findFirst();
    }

    public Todo save(Todo todo) {
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
