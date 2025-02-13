package com.example.todo.repository;

import com.example.todo.model.Priority;
import com.example.todo.model.ToDo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ToDoRepositoryTest {

    @Autowired
    private ToDoRepository toDoRepository;

    @Test
    void testSaveToDo() {
        // Arrange
        ToDo newTodo = new ToDo("Todo example", Priority.LOW);

        // Act
        newTodo = toDoRepository.save(newTodo);

        // Assert
        assertNotNull(newTodo.getId());
        assertEquals("Todo example", newTodo.getText());
    }
}
