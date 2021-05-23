package com.ToDo.backend.data;

import lombok.Data;

@Data
public class FinishedStats {

    private int finishedToday;
    private int dueToday;
    private int dueTomorrow;
    private int inProgressToday;
    private int newTasks;
}
