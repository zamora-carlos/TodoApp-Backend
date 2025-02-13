package com.example.todo.service;

import com.example.todo.model.Priority;
import com.example.todo.model.ToDo;
import com.example.todo.repository.ToDoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

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

    @Test
    void testUpdateToDo() {
        // Arrange
        ToDo todo = new ToDo("New todo", Priority.MEDIUM);
        todo.setId(1L);

        when(toDoRepository.findById(1L)).thenReturn(Optional.of(todo));
        when(toDoRepository.save(any(ToDo.class))).thenReturn(todo);

        // Act
        ToDo updatedToDo = toDoService.updateToDo(1L, "Updated text", Priority.LOW, LocalDateTime.now());

        // Assert
        assertEquals("Updated text", updatedToDo.getText());
        verify(toDoRepository, times(1)).save(todo);
        verify(toDoRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateToDo_ToDoNotFound() {
        // Arrange
        when(toDoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        ToDo updatedToDo = toDoService.updateToDo(1L, "Updated text", Priority.LOW, LocalDateTime.now());

        // Assert
        assertNull(updatedToDo);
        verify(toDoRepository, times(1)).findById(1L);
    }

    @Test
    void testMarkAsDone() {
        // Arrange
        ToDo newTodo = new ToDo("Todo", Priority.HIGH);
        newTodo.setId(1L);

        when(toDoRepository.findById(1L)).thenReturn(Optional.of(newTodo));

        // Act
        toDoService.markAsDone(1L);

        // Assert
        assertTrue(newTodo.isDone());
        assertNotNull(newTodo.getDoneDate());
        verify(toDoRepository, times(1)).findById(1L);
    }

    @Test
    void testMarkAsUndone() {
        // Arrange
        ToDo completedTodo = new ToDo("Todo", Priority.LOW);
        completedTodo.setId(1L);
        completedTodo.setDone(true);
        completedTodo.setDoneDate(LocalDateTime.of(2025, 2, 10, 0, 0));

        when(toDoRepository.findById(1L)).thenReturn(Optional.of(completedTodo));

        // Act
        toDoService.markAsUndone(1L);

        // Assert
        assertFalse(completedTodo.isDone());
        assertNull(completedTodo.getDoneDate());
        verify(toDoRepository, times(1)).findById(1L);
    }
}
