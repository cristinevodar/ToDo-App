package com.ToDo.backend.data.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class ToDoItem extends AbstractEntity implements ToDoItemSummary {



    @NotBlank(message = "title cannot be empty")
    private String title;
    private String description;
    @NotNull(message = "please  choose a priority")
    private Priority priority;
    private LocalDate dueDate;
    private LocalTime dueTime;
    private Status status;
    private String users;
    private String hash;
    private String createdBy;
    private String userEmail;
    private boolean notificationSent;



    public ToDoItem(){
        this.status=Status.NEW;
    }


    public void changeStatus(User user, Status status) {
        boolean createHistory = this.status != status && this.status != null && status != null;
        this.status = status;

    }

    public void changePriority(User user, Priority priority) {
        boolean createHistory = this.priority != priority && this.priority != null && priority != null;
        this.priority = priority;

    }
}
