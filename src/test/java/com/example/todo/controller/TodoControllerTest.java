package com.example.todo.controller;

import com.example.todo.dto.PaginatedResponse;
import com.example.todo.dto.TodoFilter;
import com.example.todo.dto.TodoResponse;
import com.example.todo.model.Priority;
import com.example.todo.service.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
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
        TodoFilter filter = new TodoFilter("task", Priority.HIGH, null);

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
                .filter(filter)
                .build();

        when(todoService.getTodos(filter, 1, 5)).thenReturn(pageResponse);

        // Act & Assert
        mockMvc.perform(get("/todos?name=Task&priority=HIGH&size=5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalItems").value(2))
                .andExpect(jsonPath("$.content[0].text").value("Task 1"));

        verify(todoService, times(1)).getTodos(filter, 1, 5);
    }
}
