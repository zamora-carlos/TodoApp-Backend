package com.example.todo.controller;

import com.example.todo.dto.*;
import com.example.todo.enums.Priority;
import com.example.todo.enums.SortCriteria;
import com.example.todo.enums.SortOrder;
import com.example.todo.service.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
public class TodoControllerTest {

    @MockBean
    private TodoService todoService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllTodos() throws Exception {
        // Arrange
        List<TodoResponse> mockContent = List.of(
                new TodoResponse(1L, "Task 1", Priority.HIGH, false, LocalDateTime.of(2025, 2, 13, 8, 20)),
                new TodoResponse(2L, "Task 2", Priority.HIGH, false, LocalDateTime.of(2025, 2, 17, 13, 0)),
                new TodoResponse(3L, "Task 3", Priority.MEDIUM, false, LocalDateTime.of(2025, 2, 10, 16, 30))
        );

        PaginatedResponse<TodoResponse> pageResponse = PaginatedResponse.<TodoResponse>builder()
                .content(mockContent)
                .currentPage(1)
                .totalPages(1)
                .pageSize(5)
                .totalItems(2)
                .build();

        when(todoService.getTodos("Task", Priority.HIGH, null, 1, 5, SortCriteria.TEXT, SortOrder.ASC)).thenReturn(pageResponse);

        // Act & Assert
        mockMvc.perform(get("/todos?text=Task&priority=HIGH&size=5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalItems").value(2))
                .andExpect(jsonPath("$.content[0].text").value("Task 1"));

        verify(todoService, times(1)).getTodos("Task", Priority.HIGH, null, 1, 5, SortCriteria.TEXT, SortOrder.ASC);
    }

    @Test
    void testCreateTodo() throws Exception {
        // Arrange
        TodoResponse todo = new TodoResponse(1L, "Title", Priority.MEDIUM, false, null);

        CreateTodoRequest todoRequest = CreateTodoRequest.builder()
                .text("Title")
                .priority(Priority.MEDIUM)
                .dueDate(null)
                .build();

        when(todoService.createTodo(any(CreateTodoRequest.class))).thenReturn(todo);

        // Act & Assert
        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.text").value("Title"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testUpdateTodo() throws Exception {
        // Arrange
        LocalDateTime date = LocalDateTime.now().plusDays(1);
        UpdateTodoRequest updateTodoRequest = new UpdateTodoRequest("Todo updated", Priority.MEDIUM, date);

        TodoResponse todoResponse = TodoResponse.builder()
                .id(1L)
                .text(updateTodoRequest.getText())
                .priority(updateTodoRequest.getPriority())
                .isDone(false)
                .dueDate(updateTodoRequest.getDueDate())
                .build();

        when(todoService.updateTodo(eq(1L), any(UpdateTodoRequest.class))).thenReturn(todoResponse);

        // Act & Assert
        mockMvc.perform(put("/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTodoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dueDate").value(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS"))))
                .andExpect(jsonPath("$.text").value(updateTodoRequest.getText()))
                .andExpect(jsonPath("$.id").value(1));

        verify(todoService, times(1)).updateTodo(eq(1L), any(UpdateTodoRequest.class));
    }
}
