package com.example.todo.service;

import com.example.todo.dto.ToDoPageResponseDto;
import com.example.todo.model.Priority;
import com.example.todo.model.ToDo;
import com.example.todo.repository.ToDoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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
    void testGetTodos() {
        // Arrange
        List<ToDo> todos = Arrays.asList(
            new ToDo("Todo 1", Priority.LOW),
            new ToDo("Todo 2", Priority.HIGH),
            new ToDo("Todo 3", Priority.MEDIUM),
            new ToDo("Todo 4", Priority.LOW)
        );

        when(toDoRepository.findAll()).thenReturn(todos);

        // Act
        ToDoPageResponseDto pageResponse = toDoService.getTodos(null, null, null, 1, 2);

        // Assert
        assertEquals(1, pageResponse.getCurrentPage());
        assertEquals(2, pageResponse.getPageSize());
        assertEquals(2, pageResponse.getData().size());
        assertEquals(4, pageResponse.getTotalItems());
        verify(toDoRepository, times(1)).findAll();
    }

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

    @Test
    void testGetAverageCompletionTime() {
        // Arrange
        List<ToDo> todos = Arrays.asList(
                new ToDo("Todo 1", Priority.LOW),
                new ToDo("Todo 2", Priority.HIGH),
                new ToDo("Todo 3", Priority.MEDIUM),
                new ToDo("Todo 4", Priority.LOW)
        );

        todos.getFirst().setDone(true);
        todos.getFirst().setCreatedAt(LocalDateTime.of(2025, 2, 10, 0, 0, 2));
        todos.getFirst().setDoneDate(todos.getFirst().getCreatedAt().plusSeconds(4000));

        todos.get(1).setDone(true);
        todos.get(1).setCreatedAt(LocalDateTime.of(2025, 1, 17, 5, 32, 2));
        todos.get(1).setDoneDate(todos.get(1).getCreatedAt().plusSeconds(231420));

        todos.get(2).setDone(true);
        todos.get(2).setCreatedAt(LocalDateTime.of(2024, 9, 14, 18, 21, 58));
        todos.get(2).setDoneDate(todos.get(2).getCreatedAt().plusSeconds(65324));

        todos.get(3).setDone(true);
        todos.get(3).setCreatedAt(LocalDateTime.of(2024, 3, 28, 12, 9, 7, 129));
        todos.get(3).setDoneDate(todos.get(3).getCreatedAt().plusSeconds(55555));

        when(toDoRepository.findAll()).thenReturn(todos);

        // Act
        long avgCompletionTime = toDoService.getAverageCompletionTime();

        // Assert
        assertEquals((long) 4000 + 231420 + 65324 + 55555, avgCompletionTime);
        verify(toDoRepository, times(1)).findAll();
    }
}
