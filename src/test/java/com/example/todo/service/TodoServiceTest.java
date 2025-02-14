package com.example.todo.service;

import com.example.todo.model.Priority;
import com.example.todo.model.Todo;
import com.example.todo.repository.TodoRepository;
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
public class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @Test
    void testGetTodos() {
        // Arrange
        List<Todo> todos = Arrays.asList(
                new Todo("Todo 1", Priority.LOW),
                new Todo("Todo 2", Priority.HIGH),
                new Todo("Todo 3", Priority.MEDIUM),
                new Todo("Todo 4", Priority.LOW));

        when(todoRepository.findAll()).thenReturn(todos);

        // Act
        TodoPageResponseDto pageResponse = todoService.getTodos(null, null, null, 1, 2);

        // Assert
        assertEquals(1, pageResponse.getCurrentPage());
        assertEquals(2, pageResponse.getPageSize());
        assertEquals(2, pageResponse.getData().size());
        assertEquals(4, pageResponse.getTotalItems());
        verify(todoRepository, times(1)).findAll();
    }

    @Test
    void testCreateTodo() {
        // Arrange
        Todo todo = new Todo("Created todo", Priority.LOW);
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        // Act
        Todo createdTodo = todoService.createTodo(todo);

        // Assert
        assertNotNull(createdTodo);
        assertEquals("Created todo", createdTodo.getText());
    }

    @Test
    void testUpdateTodo() {
        // Arrange
        Todo todo = new Todo("New todo", Priority.MEDIUM);
        todo.setId(1L);

        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        // Act
        Todo updatedTodo = todoService.updateTodo(1L, "Updated text", Priority.LOW, LocalDateTime.now());

        // Assert
        assertEquals("Updated text", updatedTodo.getText());
        verify(todoRepository, times(1)).save(todo);
        verify(todoRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateTodo_TodoNotFound() {
        // Arrange
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Todo updatedTodo = todoService.updateTodo(1L, "Updated text", Priority.LOW, LocalDateTime.now());

        // Assert
        assertNull(updatedTodo);
        verify(todoRepository, times(1)).findById(1L);
    }

    @Test
    void testMarkAsDone() {
        // Arrange
        Todo newTodo = new Todo("Todo", Priority.HIGH);
        newTodo.setId(1L);

        when(todoRepository.findById(1L)).thenReturn(Optional.of(newTodo));

        // Act
        todoService.markAsDone(1L);

        // Assert
        assertTrue(newTodo.isDone());
        assertNotNull(newTodo.getDoneDate());
        verify(todoRepository, times(1)).findById(1L);
    }

    @Test
    void testMarkAsUndone() {
        // Arrange
        Todo completedTodo = new Todo("Todo", Priority.LOW);
        completedTodo.setId(1L);
        completedTodo.setDone(true);
        completedTodo.setDoneDate(LocalDateTime.of(2025, 2, 10, 0, 0));

        when(todoRepository.findById(1L)).thenReturn(Optional.of(completedTodo));

        // Act
        todoService.markAsUndone(1L);

        // Assert
        assertFalse(completedTodo.isDone());
        assertNull(completedTodo.getDoneDate());
        verify(todoRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAverageCompletionTime() {
        // Arrange
        List<Todo> todos = Arrays.asList(
                new Todo("Todo 1", Priority.LOW),
                new Todo("Todo 2", Priority.HIGH),
                new Todo("Todo 3", Priority.MEDIUM),
                new Todo("Todo 4", Priority.LOW));

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

        when(todoRepository.findAll()).thenReturn(todos);

        // Act
        long avgCompletionTime = todoService.getAverageCompletionTime();

        // Assert
        assertEquals((long) 4000 + 231420 + 65324 + 55555, avgCompletionTime);
        verify(todoRepository, times(1)).findAll();
    }
}
