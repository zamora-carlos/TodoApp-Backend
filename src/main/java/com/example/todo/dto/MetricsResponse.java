package com.example.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
public class MetricsResponse implements Serializable {
    private long avgTime;
    private long avgTimeLow;
    private long avgTimeMedium;
    private long avgTimeHigh;
}
