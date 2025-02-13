package com.example.todo.service;

import com.example.todo.model.ToDo;
import com.example.todo.model.Priority;
import com.example.todo.repository.ToDoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ToDoService {

    @Autowired
    private ToDoRepository toDoRepository;

    public List<ToDo> getTodos(String name, Priority priority, Boolean done, int page, int size) {
        return toDoRepository.findAll();
    }

    // Implement the logic to create a new ToDo
     public ToDo createToDo(ToDo todo) {
        return toDoRepository.save(todo);
     }

    public ToDo updateToDo(Long id, String name, Priority priority, LocalDateTime dueDate) {
        Optional<ToDo> existingToDo = toDoRepository.findById(id);
        if (existingToDo.isPresent()) {
            ToDo toDo = existingToDo.get();
            toDo.setText(name);
            toDo.setPriority(priority);
            toDo.setDueDate(dueDate);
            return toDoRepository.save(toDo);
        }
        return null;
    }

    // Implement the logic to mark a ToDo as done
    //
    // public void markAsDone(Long id) { }

    public void markAsUndone(Long id) {
        Optional<ToDo> toDo = toDoRepository.findById(id);
        if (toDo.isPresent() && toDo.get().isDone()) {
            ToDo todo = toDo.get();
            todo.setDone(false);
            todo.setDoneDate(null);
            toDoRepository.save(todo);
        }
    }

    public double getAverageCompletionTime() {
        // Logic to calculate average time between creation and done, needs more work to do
        return 0.0;
    }
}
