package com.example.todo.exception;

import com.example.todo.dto.ApiErrorResponse;
import com.example.todo.dto.FieldErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Validation exceptions for query parameters 400
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(ConstraintViolationException ex) {
        List<FieldErrorResponse> errorDetails = ex.getConstraintViolations().stream()
                .map(violation -> new FieldErrorResponse(
                        violation.getPropertyPath().toString(),
                        violation.getMessage(),
                        violation.getInvalidValue()))
                .collect(Collectors.toList());

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                400, "Validation failed for one or more fields.", LocalDateTime.now(), errorDetails
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorResponse);
    }


    // Validation exceptions for request body 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<FieldErrorResponse> errorDetails = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new FieldErrorResponse(
                        fieldError.getField(),
                        fieldError.getDefaultMessage(),
                        fieldError.getRejectedValue()))
                .collect(Collectors.toList());

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                400, "Validation failed for one or more fields.", LocalDateTime.now(), errorDetails
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorResponse);
    }

    // Not found exceptions 404
    @ExceptionHandler(TodoNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleTodoNotFoundException(TodoNotFoundException ex) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                404, ex.getMessage(), LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiErrorResponse);
    }

    // General exceptions 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleExceptions(Exception ex) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                500, "An unexpected error occurred.", LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiErrorResponse);
    }
}