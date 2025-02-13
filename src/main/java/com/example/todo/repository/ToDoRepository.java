package com.example.todo.repository;

import com.example.todo.model.ToDo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ToDoRepository {

    private final List<ToDo> todos = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong(0);

    public List<ToDo> findAll() {
        return todos;
    }

    public Optional<ToDo> findById(Long id) {
        return todos.stream().filter(todo -> todo.getId().equals(id)).findFirst();
    }

    public ToDo save(ToDo todo) {
        long id = counter.getAndIncrement();
        todo.setId(id);
        todos.add(todo);
        return todo;
    }

    public void deleteAll() {
        todos.clear();
    }

    public void deleteById(Long id) {
        todos.removeIf(todo -> todo.getId().equals(id));
    }
}
