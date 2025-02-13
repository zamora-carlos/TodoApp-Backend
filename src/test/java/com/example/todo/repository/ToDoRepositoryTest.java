package com.example.todo.repository;

import com.example.todo.model.Priority;
import com.example.todo.model.ToDo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ToDoRepositoryTest {

    @Autowired
    private ToDoRepository toDoRepository;

    @BeforeEach
    void setup() {
        toDoRepository.deleteAll();
    }

    @Test
    void testFindAll() {
        // Arrange
        ToDo todo1 = new ToDo("First todo", Priority.HIGH);
        toDoRepository.save(todo1);

        ToDo todo2 = new ToDo("Second todo", Priority.MEDIUM);
        toDoRepository.save(todo2);

        // Act
        List<ToDo> todos = toDoRepository.findAll();

        for (ToDo todo : todos) {
            System.out.println(todo.getId() + " " + todo.getText());
        }

        // Assert
        assertEquals(2, todos.size());
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
}
