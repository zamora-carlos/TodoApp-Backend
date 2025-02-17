package com.example.todo.repository;

import com.example.todo.enums.Priority;
import com.example.todo.model.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    void setup() {
        todoRepository.deleteAll();
    }

    @Test
    void testFindAll() {
        // Arrange
        Todo todo1 = new Todo("First todo", Priority.HIGH);
        todoRepository.save(todo1);

        Todo todo2 = new Todo("Second todo", Priority.MEDIUM);
        todoRepository.save(todo2);

        // Act
        List<Todo> todos = todoRepository.findAll();

        // Assert
        assertEquals(2, todos.size());
    }

    @Test
    void testFindById() {
        // Arrange
        Todo todo = new Todo("Todo text", Priority.LOW);
        todoRepository.save(todo);

        // Act
        Optional<Todo> optionalTodo = todoRepository.findById(todo.getId());

        // Assert
        assertFalse(optionalTodo.isEmpty());
        assertEquals("Todo text", optionalTodo.get().getText());
    }

    @Test
    void testFindById_todoNotFound() {
        // Arrange & Act
        Optional<Todo> optionalTodo = todoRepository.findById(1L);

        // Assert
        assertTrue(optionalTodo.isEmpty());
    }

    @Test
    void testSaveTodo() {
        // Arrange
        Todo newTodo = new Todo("Example todo", Priority.LOW);

        // Act
        Todo savedTodo = todoRepository.save(newTodo);

        // Assert
        assertNotNull(savedTodo.getId());
        assertEquals("Example todo", savedTodo.getText());
    }

    @Test
    void testUpdateTodo() {
        // Arrange
        Todo todo = new Todo("New todo", Priority.LOW);
        todoRepository.save(todo);

        // Act
        todo.setPriority(Priority.HIGH);
        todoRepository.save(todo);

        Optional<Todo> optionalTodo = todoRepository.findById(todo.getId());
        List<Todo> todos = todoRepository.findAll();

        // Assert
        assertEquals(1, todos.size());
        assertTrue(optionalTodo.isPresent());
        assertEquals(Priority.HIGH, optionalTodo.get().getPriority());
    }

    @Test
    void testDeleteById() {
        // Arrange
        Todo todo = new Todo("Todo to be deleted", Priority.LOW);
        todoRepository.save(todo);

        // Act
        todoRepository.deleteById(todo.getId());
        Optional<Todo> optionalTodo = todoRepository.findById(todo.getId());

        // Assert
        assertFalse(optionalTodo.isPresent());
    }

    @Test
    void testDeleteById_todoNotFound() {
        // Arrange
        Todo todo = new Todo("Another todo", Priority.LOW);
        todoRepository.save(todo);

        // Act
        todoRepository.deleteById(todo.getId() + 1);
        Optional<Todo> optionalTodo = todoRepository.findById(todo.getId());

        // Assert
        assertTrue(optionalTodo.isPresent());
        assertEquals("Another todo", optionalTodo.get().getText());
    }
}
