package com.example.todo.repository;

import com.example.todo.model.Priority;
import com.example.todo.model.ToDo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ToDoRepositoryTest {

    @Autowired
    private ToDoRepository toDoRepository;

    @Test
    void testSaveToDo() {
        // Arrange
        ToDo newTodo = new ToDo("Example todo", Priority.LOW);

        // Act
        ToDo savedTodo = toDoRepository.save(newTodo);

        // Assert
        assertNotNull(savedTodo.getId());
        assertEquals("Example todo", savedTodo.getText());
    }

    @Test
    void testFindById() {
        // Arrange
        ToDo todo = new ToDo("Todo text", Priority.LOW);
        toDoRepository.save(todo);

        // Act
        Optional<ToDo> optionalTodo = toDoRepository.findById(todo.getId());

        // Assert
        assertFalse(optionalTodo.isEmpty());
        assertEquals("Todo text", optionalTodo.get().getText());
    }

    @Test
    void testFindById_ToDoNotFound() {
        // Arrange & Act
        Optional<ToDo> optionalTodo = toDoRepository.findById(1L);

        // Assert
        assertTrue(optionalTodo.isEmpty());
    }
}
