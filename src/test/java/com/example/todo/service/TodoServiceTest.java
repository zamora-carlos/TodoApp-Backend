package com.example.todo.service;

import com.example.todo.dto.*;
import com.example.todo.enums.*;
import com.example.todo.exception.TodoNotFoundException;
import com.example.todo.model.Todo;
import com.example.todo.repository.TodoRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

    @Nested
    public class GetTodosTests {
        
        private static List<Todo> todos;
        
        @BeforeAll
        static void setup() {
            todos = List.of(
                    new Todo("Alpha project planning", Priority.MEDIUM, LocalDateTime.now().plusDays(3)),
                    new Todo("Buy groceries", Priority.LOW, LocalDateTime.now().plusDays(1)),
                    new Todo("Complete report", Priority.HIGH, LocalDateTime.now().plusDays(2)),
                    new Todo("Draft email to team", Priority.MEDIUM, LocalDateTime.now().plusDays(5)),
                    new Todo("Exercise for 30 minutes", Priority.MEDIUM, null),
                    new Todo("Fix broken pipeline", Priority.HIGH, LocalDateTime.now().plusHours(6)),
                    new Todo("Gather feedback from stakeholders", Priority.MEDIUM, LocalDateTime.now().plusDays(4)),
                    new Todo("Host meeting", Priority.MEDIUM, null),
                    new Todo("Improve UI design", Priority.LOW, LocalDateTime.now().plusDays(7)),
                    new Todo("Join coding workshop", Priority.MEDIUM, LocalDateTime.now().plusDays(3)),
                    new Todo("Kickoff new marketing campaign", Priority.HIGH, LocalDateTime.now().plusDays(1)),
                    new Todo("Learn Spring Boot basics", Priority.LOW, null),
                    new Todo("Manage team assignment", Priority.MEDIUM, LocalDateTime.now().plusDays(10)),
                    new Todo("Notify users about changes", Priority.MEDIUM, LocalDateTime.now().plusDays(5)),
                    new Todo("Organize desk space", Priority.LOW, null),
                    new Todo("Plan upcoming sprints", Priority.MEDIUM, LocalDateTime.now().plusDays(2)),
                    new Todo("Quick bug fix", Priority.HIGH, LocalDateTime.now().plusHours(3)),
                    new Todo("Review pull request", Priority.MEDIUM, LocalDateTime.now().plusHours(12)),
                    new Todo("Schedule team outing", Priority.LOW, LocalDateTime.now().plusDays(14)),
                    new Todo("Test new feature deployment", Priority.HIGH, LocalDateTime.now().plusHours(24)),
                    new Todo("Update documentation", Priority.MEDIUM, LocalDateTime.now().plusDays(3)),
                    new Todo("Write monthly newsletter", Priority.LOW, LocalDateTime.now().plusDays(9)),
                    new Todo("Xerox meeting agenda", Priority.LOW, LocalDateTime.now().plusHours(5)),
                    new Todo("Verify backup status", Priority.MEDIUM, LocalDateTime.now().plusDays(2)),
                    new Todo("Year-end performance reviews", Priority.MEDIUM, LocalDateTime.now().plusDays(6)),
                    new Todo("Zoom conference call", Priority.MEDIUM, LocalDateTime.now().plusDays(4)),
                    new Todo("Analyze new requirements", Priority.HIGH, LocalDateTime.now().plusHours(8)),
                    new Todo("Back up project files", Priority.LOW, null),
                    new Todo("Create presentation slides", Priority.MEDIUM, LocalDateTime.now().plusDays(3)),
                    new Todo("Deploy production build", Priority.HIGH, LocalDateTime.now().plusHours(18))
            );

            todos.get(1).setDone(true);
            todos.get(4).setDone(true);
            todos.get(7).setDone(true);
            todos.get(12).setDone(true);
            todos.get(19).setDone(true);
            todos.get(24).setDone(true);
            todos.get(25).setDone(true);
        }

        @ParameterizedTest
        @CsvSource({
                "1, 2, 2, 15",
                "3, 2, 2, 15",
                "8, 4, 2, 8",
                "9, 1, 1, 30",
                "3, 9, 9, 4",
                "10, 5, 0, 6",
                "3, 12, 6, 3",
                "1, 50, 30, 1"
        })
        void testPaginationResults(int page, int pageSize, int todosOnPage, int totalPages) {
            // Arrange
            when(todoRepository.findAll()).thenReturn(todos);

            // Act
            PaginatedResponse<TodoResponse> pageResponse = todoService.getTodos(
                    null, null, null, page, pageSize, SortCriteria.TEXT, SortOrder.ASC);

            // Assert
            assertEquals(page, pageResponse.getCurrentPage());
            assertEquals(pageSize, pageResponse.getPageSize());
            assertEquals(totalPages, pageResponse.getTotalPages());
            assertEquals(todosOnPage, pageResponse.getContent().size());

            verify(todoRepository, times(1)).findAll();
        }

        @ParameterizedTest
        @CsvSource({
                "TEAM, 3",
                "x, 4",
                " , 30",
                ", 30",
                "a long text that is not in any todo, 0",
                "A, 19",
                "back, 3",
                "e r, 2"
        })
        void testFilterByText(String text, int expectedResults) {
            // Arrange
            when(todoRepository.findAll()).thenReturn(todos);

            // Act
            PaginatedResponse<TodoResponse> pageResponse = todoService.getTodos(text, null, null, 1, 30, SortCriteria.TEXT, SortOrder.ASC);
            boolean allTodosContainText = pageResponse.getContent().stream()
                    .allMatch(todo -> text == null || todo.getText().toLowerCase().contains(text.toLowerCase()));

            // Assert
            assertTrue(allTodosContainText);
            assertEquals(expectedResults, pageResponse.getContent().size());
        }

        @ParameterizedTest
        @CsvSource({"LOW, 8", "MEDIUM, 15", "HIGH, 7"})
        void testFilterByPriority(Priority priority, int expectedResults) {
            // Arrange
            when(todoRepository.findAll()).thenReturn(todos);

            // Act
            PaginatedResponse<TodoResponse> pageResponse = todoService.getTodos(null, priority, null, 1, 30, SortCriteria.TEXT, SortOrder.ASC);
            boolean allPrioritiesMatch = pageResponse.getContent().stream()
                    .allMatch(todo -> todo.getPriority() == priority);

            // Assert
            assertTrue(allPrioritiesMatch);
            assertEquals(expectedResults, pageResponse.getContent().size());
        }

        @ParameterizedTest
        @CsvSource({"true, 7", "false, 23"})
        void testFilterByDone(Boolean done, int expectedResults) {
            // Arrange
            when(todoRepository.findAll()).thenReturn(todos);

            // Act
            PaginatedResponse<TodoResponse> pageResponse = todoService.getTodos(null, null, done, 1, 30, SortCriteria.TEXT, SortOrder.ASC);
            boolean allStatusesMatch = pageResponse.getContent().stream()
                    .allMatch(todo -> done.equals(todo.isDone()));

            // Assert
            assertTrue(allStatusesMatch);
            assertEquals(expectedResults, pageResponse.getContent().size());
        }

        @ParameterizedTest
        @CsvSource({
                "ep, HIGH, ,3",
                ", LOW, true, 1",
                ", MEDIUM, true, 5",
                ", HIGH, true, 1",
                "for, ,true, 2",
                "me, MEDIUM, true, 2",
                "c, , true, 4",
                "a, MEDIUM, , 11",
                ", LOW, false, 7",
                ", MEDIUM, false, 10",
                ", HIGH, false, 6",
                "new, HIGH, , 3",
                "work, , , 1",
                "work, LOW, , 0"
        })
        void testFilterByMultipleOptions(String text, Priority priority, Boolean done, int expectedResults) {
            // Arrange
            when(todoRepository.findAll()).thenReturn(todos);

            // Act
            PaginatedResponse<TodoResponse> pageResponse = todoService.getTodos(text, priority, done, 1, 30, SortCriteria.TEXT, SortOrder.ASC);

            boolean allAttributesMatch = pageResponse.getContent().stream()
                    .allMatch(todo ->
                            (text == null || todo.getText().toLowerCase().contains(text.toLowerCase())) &&
                            (priority == null || todo.getPriority() == priority) &&
                            (done == null || done.equals(todo.isDone())));

            // Assert
            assertTrue(allAttributesMatch);
            assertEquals(expectedResults, pageResponse.getContent().size());
        }

        @ParameterizedTest
        @CsvSource({
                "TEXT, ASC, Alpha project planning",
                "TEXT, DESC, Zoom conference call",
                "PRIORITY, ASC, Xerox meeting agenda",
                "PRIORITY, DESC, Quick bug fix",
                "DUE_DATE, ASC, Quick bug fix",
                "DUE_DATE, DESC, Exercise for 30 minutes"
        })
        void testSortingPaginationResults(SortCriteria sortBy, SortOrder order, String textExpectedFirst) {
            // Arrange
            when(todoRepository.findAll()).thenReturn(todos);

            // Act
            PaginatedResponse<TodoResponse> pageResponse = todoService.getTodos(null, null, null, 1, 30, sortBy, order);

            // Assert
            assertEquals(textExpectedFirst, pageResponse.getContent().getFirst().getText());
        }
    }

    @Test
    void testCreateTodo() {
        // Arrange
        Todo todo = new Todo("Created todo", Priority.LOW);
        CreateTodoRequest todoRequest = new CreateTodoRequest("Created todo", Priority.LOW, null);
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        // Act
        TodoResponse createdTodo = todoService.createTodo(todoRequest);

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
        TodoResponse updatedTodo = todoService.updateTodo(1L, new UpdateTodoRequest("Updated text", Priority.LOW, LocalDateTime.now()));

        // Assert
        assertEquals("Updated text", updatedTodo.getText());
        verify(todoRepository, times(1)).save(todo);
        verify(todoRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateTodo_TodoNotFound() {
        // Arrange
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());
        UpdateTodoRequest updateTodoRequest = new UpdateTodoRequest("Updated text", Priority.LOW, LocalDateTime.now());

        // Act & Assert
        assertThrows(TodoNotFoundException.class, () -> todoService.updateTodo( 1L, updateTodoRequest));
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
        MetricsResponse metrics = todoService.getMetrics();

        // Assert
        assertEquals(Math.round((double) (4000 + 231420 + 65324 + 55555) / 4), metrics.getAvgTime());
        assertEquals(Math.round((double) (4000 + 55555) / 2), metrics.getAvgTimeLow());
        assertEquals(65324L, metrics.getAvgTimeMedium());
        assertEquals(231420L, metrics.getAvgTimeHigh());
        verify(todoRepository, times(4)).findAll();
    }
}
