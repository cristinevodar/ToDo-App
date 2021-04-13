package com.ToDo.backend.data.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ToDoItem extends AbstractEntity {

    public enum Status {
        Finished, InProgress, NotStarted
    }

    private String title;
    private String description;
    private String priority;
    private String deadline;
    private ToDoItem.Status status;



    public ToDoItem(String title, String description, String priority, String deadline, ToDoItem.Status status) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.status = status;
    }
}
