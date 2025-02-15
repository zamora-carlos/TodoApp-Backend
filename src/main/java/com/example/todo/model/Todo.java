package com.example.todo.model;

import com.example.todo.enums.Priority;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // This should be the unique identifier

    @Column(nullable = false, length = 120)
    private String text;

    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    private Priority priority;  // Enum reference to Priority

    private boolean isDone;

    private LocalDateTime doneDate;

    private LocalDateTime createdAt;

    // Constructors
    public Todo() {
        this.createdAt = LocalDateTime.now();
        this.isDone = false;
    }

    public Todo(String text, Priority priority) {
        this.priority = priority;
        this.text = text;
    }

    // Getters and Setters for all fields
    public Long getId() {
        return id; // Getter for the id field
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public LocalDateTime getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(LocalDateTime doneDate) {
        this.doneDate = doneDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}