package com.ToDo.ui.views.tasks;

import com.ToDo.backend.data.entity.ToDoItem;
import com.ToDo.backend.data.entity.ToDoItemSummary;
import com.vaadin.flow.data.renderer.TemplateRenderer;

import java.time.LocalDate;
import java.util.List;

import static com.ToDo.ui.utils.FormattingUtils.*;

public class TaskCard {

    public static TemplateRenderer<ToDoItem> getTemplate() {
        return TemplateRenderer.of(
                "<task-card"
                        + "  header='[[item.header]]'"
                        + "  task-card='[[item.taskCard]]'"
                        + "  on-card-click='cardClick'>"
                        + "</task-card>");
    }

    public static TaskCard create(ToDoItemSummary toDoItem){//TODO SUMMARY?
        return new TaskCard(toDoItem);
    }


    private boolean recent, inWeek;

    private final ToDoItemSummary task;

    public TaskCard(ToDoItemSummary task) {
        this.task = task;
        LocalDate now = LocalDate.now();
        LocalDate date = task.getDueDate();
        recent = date.equals(now) || date.equals(now.minusDays(1));
        inWeek = !recent && now.getYear() == date.getYear() && now.get(WEEK_OF_YEAR_FIELD) == date.get(WEEK_OF_YEAR_FIELD);
    }



    public String getTime() {
        return recent ? HOUR_FORMATTER.format(task.getDueTime()) : null;
    }

    public String getShortDay() {
        return inWeek ? SHORT_DAY_FORMATTER.format(task.getDueDate()) : null;
    }

    public String getSecondaryTime() {
        return inWeek ? HOUR_FORMATTER.format(task.getDueTime()) : null;
    }

    public String getMonth() {
        return recent || inWeek ? null : MONTH_AND_DAY_FORMATTER.format(task.getDueDate());
    }

    public String getFullDay() {
        return recent || inWeek ? null : WEEKDAY_FULLNAME_FORMATTER.format(task.getDueDate());
    }


}
