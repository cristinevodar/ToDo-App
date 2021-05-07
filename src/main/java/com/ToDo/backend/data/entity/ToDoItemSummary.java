package com.ToDo.backend.data.entity;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ToDoItemSummary {

    Long getId();

    LocalDate getDueDate();

    LocalTime getDueTime();

}
