package com.example.todo.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class TodoPageResponseDto implements Serializable {
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private int totalItems;
    private List<TodoDto> data;
}
