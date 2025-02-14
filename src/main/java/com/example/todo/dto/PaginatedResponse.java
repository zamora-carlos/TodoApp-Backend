package com.example.todo.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class PaginatedResponse<T> implements Serializable {
    private List<T> content;
    private int currentPage;
    private int totalPages;
    private int pageSize;
    private long totalItems;
}
