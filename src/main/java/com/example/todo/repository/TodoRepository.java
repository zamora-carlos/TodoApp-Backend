package com.example.todo.repository;

import com.example.todo.model.Todo;
import org.springframework.stereotype.Repository;
import com.example.todo.model.Priority;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TodoRepository {

    private final List<Todo> todos = new ArrayList<>();

    public List<Todo> findAll() {
        return todos;
    }

    public List<Todo> findAllPaginated(String name, Priority priority, Boolean done, int page, int size) {
        return todos.stream()
                .filter(todo -> (name == null || todo.getText().contains(name)) &&
                        (priority == null || todo.getPriority() == priority) &&
                        (done == null || todo.isDone() == done))
                .skip((long) page * size)
                .limit(size)
                .toList();
    }

    public Optional<Todo> findById(Long id) {
        return todos.stream().filter(todo -> todo.getId().equals(id)).findFirst();
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
