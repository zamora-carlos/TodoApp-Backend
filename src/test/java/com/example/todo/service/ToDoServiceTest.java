package com.example.todo.service;

import com.example.todo.model.Priority;
import com.example.todo.model.ToDo;
import com.example.todo.repository.ToDoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ToDoServiceTest {

    @Mock
    private ToDoRepository toDoRepository;

    @InjectMocks
    private ToDoService toDoService;

    @Test
    void testCreateToDo() {
        // Arrange
        ToDo todo = new ToDo("Created todo", Priority.LOW);
        when(toDoRepository.save(any(ToDo.class))).thenReturn(todo);

        // Act
        ToDo createdTodo = toDoService.createToDo(todo);

        // Assert
        assertNotNull(createdTodo);
        assertEquals("Created todo", createdTodo.getText());
    }
}
