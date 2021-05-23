package com.ToDo.backend.data.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class ToDoItem extends AbstractEntity implements ToDoItemSummary {



    private String title;
    private String description;
    private String priority;
    private LocalDate dueDate;
    private LocalTime dueTime;
    private Status status;

    private String userEmail;

//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @OrderColumn
//    @JoinColumn
   // private List<HistoryItem> history;

//    public void addHistoryItem(User createdBy, String comment) {
//        HistoryItem item = new HistoryItem(createdBy, comment);
//        if (history == null) {
//            history = new LinkedList<>();
//        }
//        history.add(item);
//    }
    public void addHistoryItem(User createdBy, String comment) {
        return ;
    }

    public ToDoItem(){
        this.status=Status.NEW;
    }


    public void changeStatus(User user, Status status) {
        boolean createHistory = this.status != status && this.status != null && status != null;
        this.status = status;
        if (createHistory) {
            addHistoryItem(user, "Task " + status);
        }
    }
}
