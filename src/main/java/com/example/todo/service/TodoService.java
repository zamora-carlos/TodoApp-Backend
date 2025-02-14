package com.example.todo.service;

import com.example.todo.model.Todo;
import com.example.todo.model.Priority;
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

    public List<Todo> getTodos(String name, Priority priority, Boolean done, int page, int size) {
        return todoRepository.findAllPaginated(name, priority, done, page, size);
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
